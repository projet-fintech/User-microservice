package com.banque.users_microservice.producer;


import com.banque.users_microservice.entity.Admin;
import com.banque.users_microservice.entity.Client;
import com.banque.users_microservice.entity.Employee;
import com.banque.events.UserEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    private final KafkaTemplate<String, UserEvent> KafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate){
        this.KafkaTemplate = kafkaTemplate;
    }

    public void sendClientEvent(String eventType, Client client){
        UserEvent userEvent = new UserEvent(client.getId(),client.getUsername(),client.getPassword(),eventType,"CLIENT");
        KafkaTemplate.send("user-events", client.getId().toString(), userEvent);
    }

    public void sendEmployeeEvent(String eventType, Employee employee){
        UserEvent userEvent = new UserEvent(employee.getId(),employee.getUsername(),employee.getPassword(),eventType,"EMPLOYEE");
        KafkaTemplate.send("user-events", employee.getId().toString(), userEvent);
    }

    public void sendAdminEvent(String eventType, Admin admin){
        UserEvent userEvent = new UserEvent(admin.getId(),admin.getUsername(),admin.getPassword(),eventType,"CLIENT");
        KafkaTemplate.send("user-events", admin.getId().toString(), userEvent);
    }
}
