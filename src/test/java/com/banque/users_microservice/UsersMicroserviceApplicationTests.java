package com.banque.users_microservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "notification-service.url=http://localhost:8085",
        "spring.kafka.enabled=false"
})
class UsersMicroserviceApplicationTests {

    @Test
    void contextLoads() {
    }
}
