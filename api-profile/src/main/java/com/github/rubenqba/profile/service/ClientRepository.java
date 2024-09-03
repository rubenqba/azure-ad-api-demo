package com.github.rubenqba.profile.service;

import com.github.rubenqba.profile.models.Summary;
import com.github.rubenqba.profile.models.User;
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
