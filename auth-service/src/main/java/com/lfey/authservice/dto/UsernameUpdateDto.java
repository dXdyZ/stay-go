package com.lfey.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Entity for update username",
        example = """
                {
                    "newUsername": "newUsername"
                }
                """
)
public record UsernameUpdateDto(
        @NotBlank(message = "Username must be not empty")
        @Schema(description = "New username", examples = "newUsername")
        String newUsername
) {
}
