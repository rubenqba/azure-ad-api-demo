package com.github.rubenqba.apione.ctrl;

import jakarta.validation.constraints.NotBlank;

/**
 * TeamDto summary here...
 *
 * @author rbresler
 **/
public record TeamDto(@NotBlank String name) {
}
