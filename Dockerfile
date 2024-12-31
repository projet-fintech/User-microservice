# Étape de build
FROM maven:3.9.6-eclipse-temurin-21 AS builder_service

WORKDIR /app

# Copier le repository local avec les artefacts générés
COPY repo /app/repo

# Configurer Maven pour utiliser le repository local
RUN echo "<settings><localRepository>/app/repo</localRepository></settings>" > /root/.m2/settings.xml

# Copier les fichiers nécessaires pour construire le projet
COPY pom.xml .
COPY src ./src

# Construire le projet avec Maven
RUN mvn clean install -DskipTests

# Étape pour créer l'image finale
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copier l'application jar depuis l’étape précédente
COPY --from=builder_service /app/target/*.jar app.jar

# Exposer le port
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
