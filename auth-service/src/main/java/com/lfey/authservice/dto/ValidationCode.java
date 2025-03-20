package com.lfey.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ValidationCode(
        @Email
        String email,

        @NotBlank
        String code
) {
}
