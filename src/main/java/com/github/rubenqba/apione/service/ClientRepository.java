package com.github.rubenqba.apione.service;

import com.github.rubenqba.apione.models.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.stream.Stream;

/**
 * ClientRepository summary here...
 *
 * @author rbresler
 **/
public interface ClientRepository extends MongoRepository<Client, String> {
    Stream<Client> findAllByTeam(String team);
}
