package com.banque.users_microservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Active le profil "test"
class UsersMicroserviceApplicationTests {

    @Value("${notification-service.url}")
    private String notificationServiceUrl;

    @Value("${spring.kafka.enabled}")
    private boolean kafkaEnabled;

    @Test
    void contextLoads() {
        System.out.println("Notification Service URL: " + notificationServiceUrl);
        System.out.println("Kafka Enabled: " + kafkaEnabled);
    }
}