# Étape de build
FROM maven:3.9.6-eclipse-temurin-21 AS builder_service

WORKDIR /app

# Copier les fichiers nécessaires pour construire le projet
COPY pom.xml .
COPY src ./src

# Copier l'artefact généré par events-lib depuis le workspace Jenkins
# Assurez-vous que le chemin soit correct
COPY /var/jenkins_home/workspace/events-lib/target/events-lib-1.0-SNAPSHOT.jar ./lib/events-lib.jar

# Construire le projet avec Maven
RUN mvn clean install -DskipTests

# Étape pour créer l'image finale
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copier l'application jar depuis l'étape précédente
COPY --from=builder_service /app/target/*.jar app.jar

# Copier la bibliothèque (events-lib)
COPY --from=builder_service /app/lib/*.jar lib/

# Exposer le port
EXPOSE 8080

# Lancer l'application
ENTRYPOI

