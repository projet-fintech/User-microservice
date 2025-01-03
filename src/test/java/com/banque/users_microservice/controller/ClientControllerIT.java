package com.banque.users_microservice.controller;

import com.banque.users_microservice.entity.Client;
import com.banque.users_microservice.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class ClientControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Client client;
    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(UUID.randomUUID());
        client.setUsername("testuser");
        client.setFirstName("Test");
        client.setLastName("User");
        client.setDateOfBirthday(new Date());
        client.setAge(30);
        client.setPassword("password");
        client.setNationality("TestNation");
        client.setTelephoneNumber("123-456-7890");
    }


    @Test
    void createClient_should_return_created_client() throws Exception{
        mockMvc.perform(post("/users/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(client.getId().toString()));

    }

    @Test
    void getClientById_should_return_client_when_exist() throws Exception {
        Client savedClient = clientRepository.save(client);

        mockMvc.perform(get("/users/clients/" + savedClient.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedClient.getId().toString()));

    }

    @Test
    void getClientById_should_return_not_found_when_not_exist() throws Exception{
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/users/clients/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void updateClient_should_return_updated_client() throws Exception {
        Client savedClient = clientRepository.save(client);
        Client updatedClient = new Client();
        updatedClient.setUsername("testuser2");
        updatedClient.setFirstName("Test2");
        updatedClient.setLastName("User2");
        updatedClient.setDateOfBirthday(new Date());
        updatedClient.setAge(32);
        updatedClient.setPassword("password2");
        updatedClient.setNationality("TestNation2");
        updatedClient.setTelephoneNumber("123-456-7892");
        mockMvc.perform(put("/users/clients/" + savedClient.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(updatedClient.getUsername()));
    }
    @Test
    void updateClient_should_return_not_found_when_not_exist() throws Exception {
        UUID id = UUID.randomUUID();
        Client updatedClient = new Client();
        updatedClient.setUsername("testuser2");
        updatedClient.setFirstName("Test2");
        updatedClient.setLastName("User2");
        updatedClient.setDateOfBirthday(new Date());
        updatedClient.setAge(32);
        updatedClient.setPassword("password2");
        updatedClient.setNationality("TestNation2");
        updatedClient.setTelephoneNumber("123-456-7892");

        mockMvc.perform(put("/users/clients/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedClient)))
                .andExpect(status().isNotFound());
    }


    @Test
    void deleteClient_should_return_no_content() throws Exception{
        Client savedClient = clientRepository.save(client);
        mockMvc.perform(delete("/users/clients/" + savedClient.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}