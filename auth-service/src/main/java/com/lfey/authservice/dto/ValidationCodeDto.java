package com.lfey.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(
        description = "Validation code data",
        example = """
                {
                   "email": "user@user.com",
                   "code": "123456"
                }
                """
)
public record ValidationCodeDto(

        @Email(message = "Enter the correct email address")
        @Schema(description = "Email the user who confirms the registration", example = "user@new.com")
        String email,

        @Size(min = 6, max = 6, message = "Code must be six digits long")
        @NotBlank(message = "The code field must not be empty")
        @Schema(description = "Code for confirm registration", example = "123456")
        String code
) {
}
