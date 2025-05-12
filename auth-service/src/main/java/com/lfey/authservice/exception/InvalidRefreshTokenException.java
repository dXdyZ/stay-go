package com.lfey.authservice.exception;

public class InvalidRefreshTokenException extends AuthenticationFailedException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
