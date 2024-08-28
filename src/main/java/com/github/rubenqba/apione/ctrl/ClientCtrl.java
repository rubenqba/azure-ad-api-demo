package com.github.rubenqba.apione.ctrl;

import com.github.rubenqba.apione.models.Client;
import com.github.rubenqba.apione.security.SecurityUtils;
import com.github.rubenqba.apione.service.ClientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.validation.annotation.Validated;
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
public class ClientCtrl {
    private static final Logger log = LoggerFactory.getLogger(ClientCtrl.class);

    private final ClientService service;

    public ClientCtrl(ClientService service) {
        this.service = service;
    }

    // create client using body
    @PostMapping("/clients")
    public Client create(@RequestBody @Valid Client client) {
        log.info("creando el cliente {}", client);
        return service.save(client);
    }

    @GetMapping("/clients")
    @PreAuthorize("hasAnyAuthority('AZ_Admin', 'AZ_Read')")
    public Iterable<Client> listAll() {
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

    @GetMapping("/clients/{id}")
    public Optional<Client> getClient(@PathVariable String id) {
        log.info("obteniendo el cliente con id={}", id);
        return service.findById(id);
    }

    @DeleteMapping("/clients/{id}")
    public void deleteClient(@PathVariable String id) {
        log.info("borrando el cliente con id={}", id);
        service.deleteById(id);
    }

    @PutMapping("/clients/{id}")
    public Client updateClient(@PathVariable String id, @RequestBody Client client) {
        log.info("actualizando el cliente con id={}", id);
        return service.save(client);
    }
}
