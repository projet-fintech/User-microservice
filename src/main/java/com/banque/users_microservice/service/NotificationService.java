package com.banque.users_microservice.service;


import com.banque.events.dto.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${notification-service.url}")
    private String notificationServiceUrl;

    public void sendNotification(String type, String recipientEmail, String subject, String content) {
        NotificationRequest request = new NotificationRequest(type, recipientEmail, subject, content);
        restTemplate.postForEntity(notificationServiceUrl + "/notifications/accountCreated", request, Void.class);
    }
}
