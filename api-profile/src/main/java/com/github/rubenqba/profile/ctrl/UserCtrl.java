package com.github.rubenqba.profile.ctrl;

import com.github.rubenqba.azuread.model.CustomUser;
import com.github.rubenqba.profile.ctrl.dto.CreateUserDto;
import com.github.rubenqba.profile.models.User;
import com.github.rubenqba.profile.security.SecurityUtils;
import com.github.rubenqba.profile.service.ClientService;
import com.github.rubenqba.profile.service.TeamService;
import com.github.rubenqba.profile.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * ClientCtrl summary here...
 *
 * @author rbresler
 **/
@RestController
@PreAuthorize("hasAuthority('AZ_Admin')")
public class UserCtrl {
    private static final Logger log = LoggerFactory.getLogger(UserCtrl.class);

    private final ClientService service;
    private final UserService users;
    private final TeamService teams;

    public UserCtrl(ClientService service, UserService users, TeamService teams) {
        this.service = service;
        this.users = users;
        this.teams = teams;
    }

    // create client using body
    @PostMapping("/users")
    public User create(@RequestBody @Valid CreateUserDto user) {
        log.info("creando el cliente {}", user);
        return service.save(user.firstName(), user.lastName(), user.email(), user.avatarUrl(), user.team(), user.roles());
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('AZ_Admin', 'AZ_Read')")
    public List<CustomUser> listAll() {
        if (SecurityUtils.isCurrentUserAdmin()) {
            log.info("obteniendo la lista de todos los clientes");
            return users.getUsers();
        }

        String team = SecurityUtils.getCurrentUserTeam();
        if (team != null) {
            log.info("obteniendo la lista de clientes del team '{}'", team);
            return users.getTeamUsers(team);
        }
        log.warn("usuario no tiene team asignado");
        return Collections.emptyList();
    }

    @GetMapping("/users/{id}")
    public Optional<User> getClient(@PathVariable String id) {
        log.info("obteniendo el cliente con id={}", id);
        return service.findUser(id);
    }

    @DeleteMapping("/users/{id}")
    public void deleteClient(@PathVariable String id) {
        log.info("borrando el cliente con id={}", id);
        service.deleteUser(id);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<CustomUser> updateClient(@PathVariable String id, @RequestBody CreateUserDto client) {
        log.info("actualizando el cliente con id={}", id);
//        service.updateUser(id, client.firstName(), client.lastName(), client.avatarUrl(), client.team(), client.roles());

        var team = teams.findTeam(client.team()).orElseThrow(() -> new RuntimeException("Team not found"));
        users.updateUser(id, client.firstName(), client.lastName(), team, client.roles());
        return ResponseEntity.of(users.findUser(id));
    }
}
