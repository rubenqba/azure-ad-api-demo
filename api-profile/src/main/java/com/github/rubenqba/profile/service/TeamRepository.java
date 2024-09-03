package com.github.rubenqba.profile.service;

import com.github.rubenqba.profile.models.Team;
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
