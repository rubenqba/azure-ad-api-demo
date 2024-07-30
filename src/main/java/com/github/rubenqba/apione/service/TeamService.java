package com.github.rubenqba.apione.service;

import com.github.rubenqba.apione.models.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * TeamService summary here...
 *
 * @author rbresler
 **/
@Service
public class TeamService {
    private static final Logger log = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository repository;

    public TeamService(TeamRepository repository) {
        this.repository = repository;
    }

    public boolean existsById(String id) {
        log.info("verificando si el equipo con id={} existe", id);
        return repository.existsById(id);
    }

    public Team create(Team team) {
        log.info("creando el equipo {}", team);
        return repository.save(team);
    }

    public Optional<Team> findById(String id) {
        log.info("obteniendo el equipo con id={}", id);
        return repository.findById(id);
    }

    public void deleteById(String id) {
        log.info("borrando el equipo con id={}", id);
        repository.deleteById(id);
    }

    public Iterable<Team> findAll() {
        log.info("obteniendo todos los equipos");
        return repository.findAll();
    }
}
