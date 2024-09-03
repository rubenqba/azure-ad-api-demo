package com.github.rubenqba.azuread.service;

import com.github.rubenqba.azuread.model.CustomUser;

import java.util.List;

public interface AzureClientService {

    List<CustomUser> getUsers();

    CustomUser updateUser(CustomUser user);

}
