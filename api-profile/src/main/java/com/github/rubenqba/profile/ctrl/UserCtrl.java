package com.github.rubenqba.profile.ctrl;

import com.github.rubenqba.profile.ctrl.dto.CreateUserDto;
import com.github.rubenqba.profile.models.User;
import com.github.rubenqba.profile.security.SecurityUtils;
import com.github.rubenqba.profile.service.ClientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

    public UserCtrl(ClientService service) {
        this.service = service;
    }

    // create client using body
    @PostMapping("/users")
    public User create(@RequestBody @Valid CreateUserDto user) {
        log.info("creando el cliente {}", user);
        return service.save(user.firstName(), user.lastName(), user.email(), user.avatarUrl(), user.team(), user.roles());
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('AZ_Admin', 'AZ_Read')")
    public Iterable<User> listAll() {
        if (SecurityUtils.isCurrentUserAdmin()) {
            log.info("obteniendo la lista de todos los clientes");
            return service.findAll();
        }

        String team = SecurityUtils.getCurrentUserTeam();
        if (team != null) {
            log.info("obteniendo la lista de clientes del team '{}'", team);
            return service.findAllByTeam(team);
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
    public ResponseEntity<User> updateClient(@PathVariable String id, @RequestBody CreateUserDto client) {
        log.info("actualizando el cliente con id={}", id);
        service.updateUser(id, client.firstName(), client.lastName(), client.avatarUrl(), client.team(), client.roles());
        return ResponseEntity.of(service.findUser(id));
    }
}
