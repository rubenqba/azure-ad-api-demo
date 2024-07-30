package com.github.rubenqba.apione.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

/**
 * Represents a client.
 *
 * @author rbresler
 **/
public record Client(String id, @NotBlank String firstName, @NotBlank String lastName, @Email String email, String avatarUrl, @NotBlank String team, Set<String> roles) {
}
