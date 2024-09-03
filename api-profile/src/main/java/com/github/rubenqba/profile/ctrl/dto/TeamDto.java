package com.github.rubenqba.profile.ctrl.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * TeamDto summary here...
 *
 * @author rbresler
 **/
public record TeamDto(@NotBlank String name, String plan) {
}
