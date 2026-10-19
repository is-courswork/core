FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw -q -B dependency:go-offline
COPY src src
# openapi.yaml уже в репозитории, TypeSpec в образе не собираем.
RUN ./mvnw -q -B -DskipTests -Dskip.installnodenpm -Dskip.npm package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/core-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
