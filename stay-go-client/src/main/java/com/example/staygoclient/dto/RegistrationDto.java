package com.example.staygoclient.dto;

public record RegistrationDto(
        String username,
        String email,
        String password,
        String phoneNumber
){}
