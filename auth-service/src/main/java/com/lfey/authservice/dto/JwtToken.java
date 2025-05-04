package com.lfey.authservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //Исключает пол с null в ответе
public class JwtToken {
    @NotBlank(message = "Access token must not be empty")
    private String accessToken;

    @NotBlank(message = "Refresh token must not be empty")
    private String refreshToken;
}
