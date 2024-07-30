package com.github.rubenqba.apione.service;

import com.github.rubenqba.apione.models.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * TeamRepository summary here...
 *
 * @author rbresler
 **/
public interface TeamRepository extends MongoRepository<Team, String> {
}
