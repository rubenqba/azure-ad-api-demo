package com.github.rubenqba.azuread.service;

import com.github.rubenqba.azuread.model.CustomUser;
import com.github.rubenqba.azuread.model.TeamSummary;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AzureClientService {

    List<CustomUser> getUsers();

    List<CustomUser> getTeamUsers(String team);

    Optional<CustomUser> findUser(String id);

    void deleteUser(String id);

    void updateUser(String id, String firstName, String lastName);

    void updateUserSubscription(String id, TeamSummary team, Set<String> roles);

}
