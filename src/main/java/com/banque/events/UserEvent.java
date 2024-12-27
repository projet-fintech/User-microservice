package com.banque.events;

import java.util.UUID;

public class UserEvent {
    private UUID id;
    private String username;
    private String password;
    private String eventType; //CREATE, UPDATE, DELETE
    private String role; // CLIENT, EMPLOYEE, ADMIN

    public UserEvent(UUID id, String username, String password, String eventType, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.eventType = eventType;
        this.role = role;
    }

    public UserEvent() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
