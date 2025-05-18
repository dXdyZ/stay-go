package com.example.staygoclient.dto;

public record JwtTokenDto(
        String refreshToken,
        String accessToken
) {
}
