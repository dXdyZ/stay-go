package com.lfey.authservice.exception;

public class DuplicateRoleException extends RuntimeException {
    public DuplicateRoleException(String message) {
        super(message);
    }
}
