package com.lfey.authservice.dto;

import jakarta.validation.constraints.*;

public record ValidationCode(

        @Email(message = "Enter the correct email address")
        String email,

        @Size(min = 6, max = 6, message = "Code must be six digits long")
        @NotBlank(message = "The code field must not be empty")
        String code
) {
}
