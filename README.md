# core

Бэкенд системы проверки гипотез для авиакомпаний


**Стек:** Java 21, Spring Boot 4, Spring Data JPA, Flyway, PostgreSQL 17, Maven, Lombok, MapStruct. Контракт API описывается на TypeSpec.

## Быстрый старт

Нужны JDK 21 и запущенный Docker.

```bash
./mvnw spring-boot:run
```

Spring сам поднимет Postgres из `compose.yaml`, Flyway накатит миграции. API доступно на `http://localhost:8080/api/core`:

```bash
curl -X POST localhost:8080/api/core/run -H 'Content-Type: application/json' -d '{"name":"Тест"}'
curl localhost:8080/api/core/run/<id>
curl localhost:8080/api/core/actuator/health
```

Подключение к другой БД задаётся переменными `DB_URL`, `DB_USER`, `DB_PASSWORD`.

## Контракт API и кодогенерация

Контракт — источник истины для бэкенда и фронтенда. Всё генерируется одной командой `./mvnw compile` (и любой сборкой Maven):

```
typespec/*.tsp  ──▶  src/main/resources/openapi/openapi.yaml  ──▶  Java-интерфейсы и модели
                                    │
                                    └──▶ фронтенд генерирует клиент
```

1. API описывается в `typespec/` (точка входа — `main.tsp`).
2. Maven скачивает Node.js в `target/node` (ставить его в систему не нужно) и собирает из TypeSpec `src/main/resources/openapi/openapi.yaml`. Файл **коммитится**, его использует фронтенд. CI проверяет, что он совпадает с `typespec/`.
3. Из `openapi.yaml` Maven генерирует в `target/generated-sources/openapi`:
   - интерфейсы контроллеров — `ru.itmo.airlines.core.api.*Api`;
   - модели запросов и ответов — `ru.itmo.airlines.core.api.model.*`.

   Этот код не коммитится и руками не правится.

Строки `[ERROR] Compiling...` в логе сборки — это индикатор прогресса TypeSpec, а не ошибка.

### Как добавить метод API

1. Описать модели запроса/ответа и операцию в `typespec/` (новый файл подключить в `main.tsp`).
2. `./mvnw compile` — пересоберётся `openapi.yaml` и появится новый метод в интерфейсе `*Api`.
3. Реализовать его в контроллере, при изменении схемы БД добавить миграцию `src/main/resources/db/migration/V<N>__<описание>.sql`.
4. Закоммитить вместе с изменённым `openapi.yaml`.

### Если IDE подсвечивает код красным

Чаще всего IDE не видит сгенерированные классы (`RunApi`, `RunResponse` и т.п.) или видит устаревшие. По порядку:

1. `./mvnw compile` — сгенерировать код заново.
2. IntelliJ IDEA: Maven → **Reload All Maven Projects**. Папка `target/generated-sources/openapi/src/main/java` должна быть отмечена как Generated Sources Root.
3. Не помогло — **File → Invalidate Caches… → Invalidate and Restart**.

Если красным подсвечены геттеры, сеттеры, `log` или `*MapperImpl` — IDE не обрабатывает аннотации Lombok и MapStruct: включите **Settings → Build → Compiler → Annotation Processors → Enable annotation processing** (плагин Lombok в IntelliJ встроен).

## Структура кода

Пакеты в `ru.itmo.airlines.core`:

| Пакет | Что внутри |
|---|---|
| `controller` | REST-контроллеры, реализуют сгенерированные интерфейсы `*Api` |
| `controller.advice` | Перевод исключений в ответы API (`ErrorResponse`) |
| `service` | Логика, работает с DTO |
| `dto` | DTO сервисного слоя |
| `mapper` | Интерфейсы MapStruct: перевод между моделями API, DTO и сущностями БД (реализацию генерирует MapStruct) |
| `dao.entity` | JPA-сущности (таблицы БД) |
| `dao.repository` | Репозитории Spring Data |
| `model` | Общие перечисления (например, `RunStatus`) |
| `exception` | Исключения сервиса |
| `util` | Вспомогательное: `TimeProvider` и т.п. |

Путь запроса: `CreateRunRequest` → `RunMapper` → `CreateRunDto` → `RunService` → `RunEntity` → БД, ответ обратно до `RunResponse`.

## Тесты

| Тип | Пакет | Команда | Что нужно |
|---|---|---|---|
| Юнит | `unittest` | `./mvnw test` | ничего |
| Компонентные | `componenttest` | `./mvnw verify` (запустит и юнит) | Docker: весь сервис + Postgres в Testcontainers |

## Docker-образ

```bash
docker build -t airlines-core .
docker run -p 8080:8080 -e DB_URL=jdbc:postgresql://host.docker.internal:5432/airlines airlines-core
```

## CI

`.gitlab-ci.yml`: проверка, что `openapi.yaml` собран из актуального `typespec/`, затем сборка jar с юнит-тестами.
