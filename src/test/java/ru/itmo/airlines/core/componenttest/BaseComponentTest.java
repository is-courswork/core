package ru.itmo.airlines.core.componenttest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

/** Базовый класс компонентных тестов: весь сервис целиком + Postgres в контейнере. */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public abstract class BaseComponentTest {

    @Autowired
    protected MockMvc mockMvc;
}
