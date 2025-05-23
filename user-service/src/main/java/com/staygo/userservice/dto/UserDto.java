package com.staygo.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entity for save user",
        example = """
                {
                    "email": "user@email.com",
                    "phoneNumber": "899999999",
                    "username": "user"
                }
                """
)
public class UserDto implements Serializable {
    private static final Long UUID = 1L;
    @Schema(description = "User email address", example = "user@email.com")
    private String email;
    @Schema(description = "User phone number", example = "899999999")
    private String phoneNumber;
    @Schema(description = "Unique user name and login", example = "user")
    private String username;
}