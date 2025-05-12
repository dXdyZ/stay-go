package com.lfey.authservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //Исключает пол с null в ответе
@Schema(
        description = "Used for return user data",
        example = """
                {
                    "username": "user",
                    "phoneNumber": "890000000",
                    "email": "user@email.com"
                }
                """
)
public class UserDetailsDto implements Serializable {
    @Schema(description = "Username", example = "user")
    private String username;
    @Schema(description = "Phone number", example = "890000000")
    private String phoneNumber;
    @Schema(description = "Email address", example = "user@email.com")
    private String email;
}
