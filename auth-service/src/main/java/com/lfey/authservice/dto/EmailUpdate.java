package com.lfey.authservice.dto;

import jakarta.validation.constraints.Email;

public record EmailUpdate(
        @Email(message = "Enter the correct email address")
        String email
) {
}
