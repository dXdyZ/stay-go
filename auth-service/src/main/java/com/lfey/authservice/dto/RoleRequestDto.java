package com.lfey.authservice.dto;

import com.lfey.authservice.entity.jpa.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Role name",
        example = """
                {
                    "role": "ROlE_USER"
                }
                """
)
public record RoleRequestDto(
        @NotBlank(message = "Role name must not be empty")
        @Schema(description = "User role",
                allowableValues = {"ROLE_USER", "ROLE_ADMIN", "ROLE_OWNER", "ROLE_MANAGER"},
                example = "ROLE_USER"
        )
        RoleName role
) {
}
