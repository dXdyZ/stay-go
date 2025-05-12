package com.lfey.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Entity with new password",
        example = """
                {
                    "newPassword": "password"
                }
                """
)
public record ResetPasswordRequestDto(
        @NotBlank(message = "New password must not be empty")
        @Schema(description = "New password", example = "password")
        String newPassword
) {
}
