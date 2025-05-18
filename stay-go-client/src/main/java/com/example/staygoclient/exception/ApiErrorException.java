package com.example.staygoclient.exception;

import com.example.staygoclient.dto.ErrorResponse;
import lombok.Getter;

@Getter
public class ApiErrorException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public ApiErrorException(ErrorResponse errorResponse) {
        super("API Error: " + errorResponse.code());
        this.errorResponse = errorResponse;
    }
}
