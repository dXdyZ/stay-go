package com.lfey.authservice.controller;

import com.lfey.authservice.exception.DuplicateUserException;
import com.lfey.authservice.exception.InvalidCodeException;
import com.lfey.authservice.exception.UserCacheDataNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<?> handleInvalidCode(InvalidCodeException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }

    @ExceptionHandler(UserCacheDataNotFoundException.class)
    public ResponseEntity<?> handleUserRegNotFound(UserCacheDataNotFoundException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<?> handleDuplicateUserException(DuplicateUserException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }
}
