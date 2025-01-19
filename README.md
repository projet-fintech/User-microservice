---

### **Users Microservice (README)**
![image](https://github.com/user-attachments/assets/161d05bc-5cdf-4079-b864-83e5365a70c6)


```markdown
# Users Microservice


## Description
Ce microservice gère la création, la mise à jour, et la suppression des utilisateurs. Il communique avec le microservice Authentication via Kafka pour synchroniser les données utilisateur.

## Fonctionnalités
- CRUD sur les utilisateurs.
- Synchronisation des utilisateurs avec Authentication via Kafka.
-communication avec Notification Microservice via Rest.

## Dépendances
- Spring Boot
- Kafka
- Eureka Client

## Configuration
- Configurez Kafka dans `application.yml` :
  - `kafka.bootstrap-servers`

## Lancer le service
```bash
mvn clean install
java -jar target/users-service.jar
