package com.lfey.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        String username,

        @NotBlank
        String password
) {
}
