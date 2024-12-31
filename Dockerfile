# Étape de build
FROM maven:3.9.6-eclipse-temurin-21 AS builder_service

WORKDIR /app

# Copier le fichier events-lib depuis le contexte Docker
COPY events-lib-1.0-SNAPSHOT.jar ./lib/events-lib.jar

# Copier les fichiers nécessaires pour construire le projet
COPY pom.xml .
COPY src ./src

# Configurer Maven pour utiliser le repository local
RUN mvn clean install -DskipTests -Dmaven.repo.local=/app/repo

# Étape pour créer l'image finale
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copier l'application jar depuis l'étape précédente
COPY --from=builder_service /app/target/*.jar app.jar

# Copier la bibliothèque (events-lib) depuis l'étape précédente
COPY --from=builder_service /app/lib/*.jar lib/

# Exposer le port
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
