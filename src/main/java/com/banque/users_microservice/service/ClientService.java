package com.banque.users_microservice.service;


import com.banque.users_microservice.entity.Client;
import com.banque.users_microservice.producer.UserEventProducer;
import com.banque.users_microservice.repository.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserEventProducer userEventProducer;
    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository, UserEventProducer userEventProducer, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.userEventProducer= userEventProducer;
        this.passwordEncoder = passwordEncoder;
    }

    public Client createClient(Client client) {
        Client newClient = new Client();
        newClient.setUsername(client.getUsername());
        newClient.setFirstName(client.getFirstName());
        newClient.setLastName(client.getLastName());
        newClient.setDateOfBirthday(client.getDateOfBirthday());
        newClient.setAge(client.getAge());
        newClient.setPassword(passwordEncoder.encode(client.getPassword()));
        newClient.setNationality(client.getNationality());
        newClient.setTelephoneNumber(client.getTelephoneNumber());
        newClient.setAccountsIDs(client.getAccountsIDs());
        newClient.setStatus(client.getStatus());

        clientRepository.save(newClient);
        userEventProducer.sendClientEvent("CREATED",newClient);

        return newClient;
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
            existingClient.setPassword(passwordEncoder.encode(client.getPassword()));
            existingClient.setNationality(client.getNationality());
            existingClient.setTelephoneNumber(client.getTelephoneNumber());
            existingClient.setAccountsIDs(client.getAccountsIDs());
            existingClient.setStatus(client.getStatus());
            clientRepository.save(existingClient);
            userEventProducer.sendClientEvent("UPDATED",existingClient);
            return existingClient;
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
