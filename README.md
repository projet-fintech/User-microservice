---

### **Users Microservice (README)**

```markdown
# Users Microservice

![image](https://github.com/user-attachments/assets/ddc9b3b3-eed7-4fea-a741-a51793167ae1)


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
