package com.lfey.authservice.controller;

import com.lfey.authservice.exception.InvalidCodeException;
import com.lfey.authservice.exception.UserRegNotFoundException;
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

    @ExceptionHandler(UserRegNotFoundException.class)
    public ResponseEntity<?> handleUserRegNotFound(UserRegNotFoundException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }
}
