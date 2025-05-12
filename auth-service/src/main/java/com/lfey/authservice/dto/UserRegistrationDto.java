package com.lfey.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "User registration data",
        example = """
                {
                    "username": "user",
                    "email": "user@email.com",
                    "password": "password",
                    "phoneNumber": "89000000"
                }
                """
)
public record UserRegistrationDto(
        @NotBlank(message = "Username must not be empty")
        String username,
        @Email(message = "Enter correct email")
        String email,
        @NotBlank(message = "Password must not be empty")
        String password,
        @NotBlank(message = "Phone number must not be empty")
        String phoneNumber
) {
}
