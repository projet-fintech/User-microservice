package com.banque.users_microservice.service;


import com.banque.users_microservice.entity.Client;
import com.banque.users_microservice.entity.Status; // Importez l'enum ou la classe Status
import com.banque.users_microservice.producer.UserEventProducer;
import com.banque.users_microservice.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserEventProducer userEventProducer;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ClientService clientService;

    private SimpleDateFormat dateFormat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Test
    void testCreateClient() throws ParseException {
        // Arrange
        Client client = new Client();
        client.setUsername("john.doe");
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setDateOfBirthday(dateFormat.parse("1990-01-01")); // Conversion en Date
        client.setAge(33);
        client.setPassword("password");
        client.setNationality("American");
        client.setTelephoneNumber("1234567890");
        client.setAccountsIDs(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
        client.setStatus(Status.ACTIF); // Utilisation de l'enum Status

        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Act
        Client createdClient = clientService.createClient(client);

        // Assert
        assertNotNull(createdClient);
        assertEquals("john.doe", createdClient.getUsername());
        assertEquals("John", createdClient.getFirstName());
        assertEquals("Doe", createdClient.getLastName());
        assertEquals(dateFormat.parse("1990-01-01"), createdClient.getDateOfBirthday()); // Vérification de la date
        assertEquals(Status.ACTIF, createdClient.getStatus()); // Vérification du statut
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(userEventProducer, times(1)).sendClientEvent("CREATED", createdClient);
    }

    @Test
    void testGetClientById() throws ParseException {
        // Arrange
        UUID id = UUID.randomUUID();
        Client client = new Client();
        client.setId(id);
        client.setUsername("john.doe");
        client.setDateOfBirthday(dateFormat.parse("1990-01-01")); // Conversion en Date
        client.setStatus(Status.ACTIF); // Utilisation de l'enum Status

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        // Act
        Optional<Client> foundClient = clientService.getClientById(id);

        // Assert
        assertTrue(foundClient.isPresent());
        assertEquals(id, foundClient.get().getId());
        assertEquals("john.doe", foundClient.get().getUsername());
        assertEquals(dateFormat.parse("1990-01-01"), foundClient.get().getDateOfBirthday()); // Vérification de la date
            assertEquals(Status.ACTIF, foundClient.get().getStatus()); // Vérification du statut
        verify(clientRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateClient() throws ParseException {
        // Arrange
        UUID id = UUID.randomUUID();
        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setUsername("john.doe");
        existingClient.setDateOfBirthday(dateFormat.parse("1990-01-01")); // Conversion en Date
        existingClient.setStatus(Status.ACTIF); // Utilisation de l'enum Status

        Client updatedClient = new Client();
        updatedClient.setUsername("john.doe.updated");
        updatedClient.setFirstName("John Updated");
        updatedClient.setDateOfBirthday(dateFormat.parse("1995-05-05")); // Conversion en Date
        updatedClient.setStatus(Status.INACTIF); // Utilisation de l'enum Status

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenReturn(updatedClient);

        // Act
        Client result = clientService.updateClient(id, updatedClient);

        // Assert
        assertNotNull(result);
        assertEquals("john.doe.updated", result.getUsername());
        assertEquals("John Updated", result.getFirstName());
        assertEquals(dateFormat.parse("1995-05-05"), result.getDateOfBirthday()); // Vérification de la date
        assertEquals(Status.INACTIF, result.getStatus()); // Vérification du statut
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(userEventProducer, times(1)).sendClientEvent("UPDATED", result);
    }

    @Test
    void testUpdateClient_NotFound() throws ParseException {
        // Arrange
        UUID id = UUID.randomUUID();
        Client updatedClient = new Client();
        updatedClient.setUsername("john.doe.updated");
        updatedClient.setDateOfBirthday(dateFormat.parse("1995-05-05")); // Conversion en Date
        updatedClient.setStatus(Status.ACTIF); // Utilisation de l'enum Status

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Client result = clientService.updateClient(id, updatedClient);

        // Assert
        assertNull(result);
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, never()).save(any(Client.class));
        verify(userEventProducer, never()).sendClientEvent(anyString(), any(Client.class));
    }

    @Test
    void testDeleteClient() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        clientService.deleteClient(id);

        // Assert
        verify(clientRepository, times(1)).deleteById(id);
        verify(userEventProducer, times(1)).sendClientEvent(eq("DELETED"), argThat(client -> id.equals(client.getId())));
    }

    @Test
    void testGetAllClients() throws ParseException {
        // Arrange
        Client client1 = new Client();
        client1.setUsername("john.doe");
        client1.setDateOfBirthday(dateFormat.parse("1990-01-01")); // Conversion en Date
        client1.setStatus(Status.ACTIF); // Utilisation de l'enum Status

        Client client2 = new Client();
        client2.setUsername("jane.doe");
        client2.setDateOfBirthday(dateFormat.parse("1995-05-05")); // Conversion en Date
        client2.setStatus(Status.INACTIF); // Utilisation de l'enum Status

        List<Client> clients = Arrays.asList(client1, client2);

        when(clientRepository.findAll()).thenReturn(clients);

        // Act
        List<Client> result = clientService.getAllClients();

        // Assert
        assertEquals(2, result.size());
        assertEquals(dateFormat.parse("1990-01-01"), result.get(0).getDateOfBirthday()); // Vérification de la date
        assertEquals(Status.ACTIF, result.get(0).getStatus()); // Vérification du statut
        assertEquals(dateFormat.parse("1995-05-05"), result.get(1).getDateOfBirthday()); // Vérification de la date
        assertEquals(Status.INACTIF, result.get(1).getStatus()); // Vérification du statut
        verify(clientRepository, times(1)).findAll();
    }


}