package com.github.rubenqba.apione.service;

import com.github.rubenqba.apione.models.Client;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * ClientService summary here...
 *
 * @author rbresler
 **/
@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public Client save(Client client) {
        return repository.save(client);
    }

    public Optional<Client> findById(String id) {
        return repository.findById(id);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public Iterable<Client> findAll() {
        return repository.findAll();
    }
}
