package com.github.rubenqba.profile.ctrl.dto;

import jakarta.validation.constraints.NotEmpty;

public record CreatePlanDto(@NotEmpty String name, String description) {
}
