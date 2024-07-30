package com.github.rubenqba.apione.ctrl;

import com.github.rubenqba.apione.models.Team;
import com.github.rubenqba.apione.service.TeamService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * TeamCtrl summary here...
 *
 * @author rbresler
 **/
@RestController
@Validated
public class TeamCtrl {
    private static final Logger log = LoggerFactory.getLogger(TeamCtrl.class);

    private final TeamService service;

    public TeamCtrl(TeamService service) {
        this.service = service;
    }

    // create a team using body
    @PostMapping("/teams")
    public Team create(@RequestBody @Valid TeamDto team) {
        log.info("creando el equipo {}", team);
        return service.create(new Team(null, team.name()));
    }

    // get all teams
    @GetMapping("/teams")
    public Iterable<Team> findAll() {
        log.info("obteniendo todos los equipos");
        return service.findAll();
    }

    // get team by id
    @GetMapping("/teams/{id}")
    public ResponseEntity<Team> findTeam(@PathVariable String id) {
        log.info("obteniendo el equipo con id={}", id);
        return ResponseEntity.of(service.findById(id));
    }

    // delete team by id
    @DeleteMapping("/teams/{id}")
    public void deleteTeam(@PathVariable String id) {
        log.info("borrando el equipo con id={}", id);
        service.deleteById(id);
    }
}
