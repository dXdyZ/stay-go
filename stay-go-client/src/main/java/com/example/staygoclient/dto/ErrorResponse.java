package com.example.staygoclient.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        Map<String, String> error,
        Integer code
){}
