package com.lfey.authservice.dto;

public record ValidationCode(
        String email,
        String code
) {
}
