package com.lfey.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UsernameUpdate(
        @NotBlank(message = "Username must be not empty")
        String newUsername
) {
}
