package com.github.rubenqba.apione.ctrl;

import jakarta.validation.constraints.NotEmpty;

public record CreatePlanDto(@NotEmpty String name, String description) {
}
