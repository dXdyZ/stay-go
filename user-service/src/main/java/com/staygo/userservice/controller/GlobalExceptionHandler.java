package com.staygo.userservice.controller;

import com.staygo.userservice.exception.DuplicateUserException;
import com.staygo.userservice.exception.ErrorResponse;
import com.staygo.userservice.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .errorCode(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserException(DuplicateUserException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .errorCode(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .build());
    }
}
