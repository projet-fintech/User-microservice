package com.banque.users_microservice.service;

import com.banque.users_microservice.entity.Client;
import com.banque.users_microservice.entity.Status;
import com.banque.users_microservice.producer.UserEventProducer;
import com.banque.users_microservice.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserEventProducer userEventProducer;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientService clientService;

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
        client.setStatus(Status.ACTIF);
        client.setAccountsIDs(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
    }


    @Test
    void createClient_should_return_created_client(){
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        Client createdClient = clientService.createClient(client);
        assertNotNull(createdClient);
        assertEquals(client.getId(),createdClient.getId());
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(userEventProducer, times(1)).sendClientEvent(eq("CREATED"), any(Client.class));
    }


    @Test
    void getClientById_should_return_client_when_exist() {
        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));

        Optional<Client> foundClient = clientService.getClientById(client.getId());
        assertTrue(foundClient.isPresent());
        assertEquals(client.getId(), foundClient.get().getId());
        verify(clientRepository, times(1)).findById(client.getId());
    }

    @Test
    void getClientById_should_return_empty_when_not_exist() {
        UUID id = UUID.randomUUID();
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Client> foundClient = clientService.getClientById(id);
        assertFalse(foundClient.isPresent());
        verify(clientRepository, times(1)).findById(id);
    }


    @Test
    void getAllClients() {
        when(clientRepository.findAll()).thenReturn(Arrays.asList(client, new Client()));

        List<Client> clients = clientService.getAllClients();

        assertEquals(2, clients.size());
        verify(clientRepository, times(1)).findAll();
    }


    @Test
    void updateClient_should_return_updated_client_when_exist() {
        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        Client newClient = new Client();
        newClient.setUsername("testuser2");
        newClient.setFirstName("Test2");
        newClient.setLastName("User2");
        newClient.setDateOfBirthday(new Date());
        newClient.setAge(32);
        newClient.setPassword("password2");
        newClient.setNationality("TestNation2");
        newClient.setTelephoneNumber("123-456-7892");
        newClient.setStatus(Status.INACTIF);
        newClient.setAccountsIDs(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));

        when(clientRepository.save(any(Client.class))).thenReturn(newClient);


        Client updateClient = clientService.updateClient(client.getId(), newClient);
        assertNotNull(updateClient);
        assertEquals(newClient.getUsername(),updateClient.getUsername());
        verify(clientRepository, times(1)).findById(client.getId());
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(userEventProducer, times(1)).sendClientEvent(eq("UPDATED"), any(Client.class));
    }

    @Test
    void updateClient_should_return_null_when_not_exist() {
        UUID id = UUID.randomUUID();
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        Client newClient = new Client();
        newClient.setUsername("testuser2");
        newClient.setFirstName("Test2");
        newClient.setLastName("User2");
        newClient.setDateOfBirthday(new Date());
        newClient.setAge(32);
        newClient.setPassword("password2");
        newClient.setNationality("TestNation2");
        newClient.setTelephoneNumber("123-456-7892");
        newClient.setStatus(Status.INACTIF);
        newClient.setAccountsIDs(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));

        Client updateClient = clientService.updateClient(id, newClient);
        assertNull(updateClient);
        verify(clientRepository, times(1)).findById(id);

    }
    @Test
    void deleteClient() {
        doNothing().when(clientRepository).deleteById(client.getId());
        clientService.deleteClient(client.getId());
        verify(clientRepository, times(1)).deleteById(client.getId());
        verify(userEventProducer, times(1)).sendClientEvent(eq("DELETED"), any(Client.class));
    }

}