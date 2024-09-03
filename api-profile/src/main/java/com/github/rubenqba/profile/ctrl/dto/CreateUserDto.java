package com.github.rubenqba.profile.ctrl.dto;

import java.util.Set;

/**
 * CreateUserDto summary here...
 *
 * @author rbresler
 **/
public record CreateUserDto(String firstName, String lastName, String email, String avatarUrl, String team, Set<String> roles) {
}
