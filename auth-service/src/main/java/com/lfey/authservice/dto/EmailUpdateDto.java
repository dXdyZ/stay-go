package com.lfey.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "Entity with new email",
        example = """
                {
                    "email": "newEmail@mail.com"
                }
                """
)
public record EmailUpdateDto(
        @Email(message = "Enter the correct email address")
        @Schema(description = "New email address", examples = "newEmail@mail.com")
        String email
) {
}
