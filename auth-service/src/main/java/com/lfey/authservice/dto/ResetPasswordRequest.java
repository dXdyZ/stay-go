package com.lfey.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank(message = "New password must not be empty")
        String newPassword,
        @NotBlank(message = "Username must not be empty")
        String username
) {
}
