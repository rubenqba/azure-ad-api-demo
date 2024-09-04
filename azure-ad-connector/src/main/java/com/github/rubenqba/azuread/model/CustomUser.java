package com.github.rubenqba.azuread.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.Set;

public record CustomUser(
        String id,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email
        String email,
        String avatarUrl,
        TeamSummary team,
        Set<String> roles,
        Instant createdAt) {
}
