package com.github.rubenqba.profile.service;

import com.github.rubenqba.azuread.model.CustomUser;
import com.github.rubenqba.azuread.model.TeamSummary;
import com.github.rubenqba.azuread.service.AzureClientService;
import com.github.rubenqba.profile.models.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * UserService summary here...
 *
 * @author rbresler
 **/
@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final AzureClientService azure;


    public UserService(AzureClientService azure) {
        this.azure = azure;
    }

    public List<CustomUser> getUsers() {
        log.info("recovering azure users...");
        return azure.getUsers();
    }

    public Optional<CustomUser> findUser(String id) {
        return azure.findUser(id);
    }

    public void deleteUser(String id) {
        log.info("removing azure user '{}'", id);
        azure.deleteUser(id);
    }

    public void assignUserToTeam(String id, TeamSummary team, Set<String> roles) {
        log.info("assign user '{}' to team '{}' with roles '{}'", id, team, roles);
        azure.updateUserSubscription(id, team, roles);
    }

    public void updateUser(String id, String firstName, String lastName, Team team, Set<String> roles) {
        log.info("updating user '{}': firstName '{}' and lastName '{}'", id, firstName, lastName);
        azure.updateUser(id, firstName, lastName);
        assignUserToTeam(id, new TeamSummary(team.getId(), team.getName(), team.getPlan().id()), roles);
    }

    public List<CustomUser> getTeamUsers(String team) {
        log.info("recovering azure users for team '{}'", team);
        return azure.getTeamUsers(team);
    }
}
