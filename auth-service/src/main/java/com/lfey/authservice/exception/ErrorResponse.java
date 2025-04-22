package com.lfey.authservice.exception;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        String message,
        Integer code
) {
}
