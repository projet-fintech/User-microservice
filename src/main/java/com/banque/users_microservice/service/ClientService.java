package com.banque.users_microservice.service;


import com.banque.users_microservice.entity.Client;
import com.banque.users_microservice.producer.UserEventProducer;
import com.banque.users_microservice.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserEventProducer userEventProducer;


    public ClientService(ClientRepository clientRepository, UserEventProducer userEventProducer) {
        this.clientRepository = clientRepository;
        this.userEventProducer= userEventProducer;

    }

    public Client createClient(Client client) {
        Client newClient = new Client();
        newClient.setUsername(client.getUsername());
        newClient.setFirstName(client.getFirstName());
        newClient.setLastName(client.getLastName());
        newClient.setDateOfBirthday(client.getDateOfBirthday());
        newClient.setAge(client.getAge());
        newClient.setPassword(client.getPassword());
        newClient.setNationality(client.getNationality());
        newClient.setTelephoneNumber(client.getTelephoneNumber());
        newClient.setAccountsIDs(client.getAccountsIDs());
        newClient.setStatus(client.getStatus());

        Client savedClient = clientRepository.save(newClient);
        userEventProducer.sendClientEvent("CREATED",savedClient);

        return savedClient;
    }

    public Optional<Client> getClientById(UUID id) {
        return clientRepository.findById(id);
    }

    public Client updateClient(UUID id, Client client){
        Client existingClient = clientRepository.findById(id).orElse(null);
        if (existingClient != null) {
            existingClient.setUsername(client.getUsername());
            existingClient.setFirstName(client.getFirstName());
            existingClient.setLastName(client.getLastName());
            existingClient.setDateOfBirthday(client.getDateOfBirthday());
            existingClient.setAge(client.getAge());
            existingClient.setPassword(client.getPassword());
            existingClient.setNationality(client.getNationality());
            existingClient.setTelephoneNumber(client.getTelephoneNumber());
            existingClient.setAccountsIDs(client.getAccountsIDs());
            existingClient.setStatus(client.getStatus());
            Client savedClient = clientRepository.save(existingClient);
            userEventProducer.sendClientEvent("UPDATED",savedClient);
            return savedClient;
        }
        return null;
    }

    public void deleteClient(UUID id) {
        clientRepository.deleteById(id);
        userEventProducer.sendClientEvent("DELETED", new Client(id));
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}