package com.github.rubenqba.apione.service;

import com.github.rubenqba.apione.models.Summary;
import com.github.rubenqba.apione.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.stream.Stream;

/**
 * ClientRepository summary here...
 *
 * @author rbresler
 **/
public interface ClientRepository extends MongoRepository<User, String> {
    Stream<User> findAllByTeam(Summary team);
}
