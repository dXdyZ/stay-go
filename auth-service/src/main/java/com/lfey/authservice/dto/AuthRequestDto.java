package com.lfey.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        description = "Authorization data",
        example = """
                {
                    "username": "user",
                    "password": "password"
                }
                """
)
public record AuthRequestDto(

        @NotBlank(message = "Username must not be empty")
        @Schema(description = "Username", example = "user")
        String username,

        @NotBlank(message = "Password must not be empty")
        @Schema(description = "Password", example = "password")
        String password
) {
}
