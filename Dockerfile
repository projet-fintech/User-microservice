FROM maven:3.9.6-eclipse-temurin-21 AS builder_service

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN  mvn clean install -DskipTests

 # Construction de l'image finale
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder_service /app/target/*.jar app.jar
COPY --from=builder_service /app/lib/*.jar lib/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
