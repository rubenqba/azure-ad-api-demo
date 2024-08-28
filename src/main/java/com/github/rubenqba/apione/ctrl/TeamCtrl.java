package com.github.rubenqba.apione.ctrl;

import com.github.rubenqba.apione.models.Team;
import com.github.rubenqba.apione.security.SecurityUtils;
import com.github.rubenqba.apione.service.TeamService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * TeamCtrl summary here...
 *
 * @author rbresler
 **/
@RestController
@Validated
@PreAuthorize("hasAuthority('AZ_Admin')")
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
        return service.create(new Team(null, team.name(), null));
    }

    @PutMapping("/teams/{id}")
    public Team update(@PathVariable String id, @RequestBody @Valid TeamDto team) {
        log.info("actualizando el equipo {}", team);
        return service.update(id, new Team(null, team.name(), team.plan()));
    }

    // get all teams
    @PreAuthorize("hasAnyAuthority('AZ_Admin', 'AZ_Read')")
    @GetMapping("/teams")
    public Iterable<Team> findAll(JwtAuthenticationToken auth) {
        if (SecurityUtils.isAdmin(auth.getAuthorities())) {
            log.info("obteniendo todos los equipos");
            return service.findAll();
        }

        var team = SecurityUtils.getUserTeam(auth.getToken());
        if (team != null) {
            log.info("obteniendo todos los equipos");
            return service.findAll();
        }
        throw new AccessDeniedException("User has no team");
    }

    // get team by id
    @GetMapping("/teams/{id}")
    @PreAuthorize("hasAnyAuthority('AZ_Admin', 'AZ_Read')")
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
