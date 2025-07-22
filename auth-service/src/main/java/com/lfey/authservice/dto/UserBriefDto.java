package com.lfey.authservice.dto;

import com.lfey.authservice.entity.jpa.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Brief user information",
        example = """
                {
                    "username": "user123",
                    "roles": ["ROLE_USER"]
                }""")
public class UserBriefDto {

    @Schema(
            description = "Unique username of the user",
            example = "user123"
    )
    private String username;

    @Schema(
            description = "List of user roles",
            allowableValues = {"ROLE_USER", "ROLE_ADMIN", "ROLE_OWNER", "ROLE_MANAGER"},
            example = "[\"ROLE_USER\"]",
            type = "array"
    )
    private List<RoleName> roles;
}

