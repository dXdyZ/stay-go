package com.staygo.userservice.exception;

public class FeignCustomServerErrorException extends RuntimeException{
    private final ErrorResponse errorResponse;

    public FeignCustomServerErrorException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return this.errorResponse;
    }
}
