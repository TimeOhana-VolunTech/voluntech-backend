# Estágio de Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean install -DskipTests

# Estágio de Execução
FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /target/voluntech-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]