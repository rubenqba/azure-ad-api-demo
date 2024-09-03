package com.github.rubenqba.profile.service;

import com.github.rubenqba.profile.models.Summary;
import com.github.rubenqba.profile.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * ClientService summary here...
 *
 * @author rbresler
 **/
@Service
public class ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public User save(String firstName, String lastName, String email, String avatarUrl, String team, Set<String> roles) {
        return repository.save(User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .avatarUrl(avatarUrl)
                        .team(new Summary(team, null))
                        .roles(roles)
                .build());
    }

    public Optional<User> findUser(String id) {
        return repository.findById(id);
    }

    public void deleteUser(String id) {
        repository.deleteById(id);
    }

    public Iterable<User> findAll() {
        return repository.findAll();
    }

    public Iterable<User> findAllByTeam(String team) {
        return repository.findAllByTeam(new Summary(team, null)).toList();
    }

    public void updateUser(String id, String firstname, String lastname, String avatarUrl, String team, Set<String> roles) {
        log.info("updating user '{}'", id);

        final var user = findUser(id);
        user.ifPresent(p -> {
            p.setFirstName(firstname);
            p.setLastName(lastname);
            p.setAvatarUrl(avatarUrl);
            p.setTeam(new Summary(team, null));
            p.setRoles(roles);
            repository.save(p);
        });
    }
}
