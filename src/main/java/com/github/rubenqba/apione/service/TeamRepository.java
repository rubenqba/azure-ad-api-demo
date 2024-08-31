package com.github.rubenqba.apione.service;

import com.github.rubenqba.apione.models.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * TeamRepository summary here...
 *
 * @author rbresler
 **/
public interface TeamRepository extends MongoRepository<Team, String> {
    <T> List<T> findAllBy(Class<T> type);
}
