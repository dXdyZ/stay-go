package com.staygo.userservice.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

@Schema(description = "Error response",
        example = """
                    {
                       "timestamp": "2025-05-04 04:02:20.626585",
                       "message": {
                           "message": "Error message"
                       },
                       "code": "400" 
                    }
                    """
)
public record ErrorResponse(
    Instant timestamp,
    Map<String, String> error,
    Integer errorCode
){}
