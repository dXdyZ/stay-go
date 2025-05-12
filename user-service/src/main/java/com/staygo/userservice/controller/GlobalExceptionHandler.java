package com.staygo.userservice.controller;

import com.staygo.userservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(),
                        Map.of("message", exception.getMessage()),
                        HttpStatus.NOT_FOUND.value()
                ));
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserException(DuplicateUserException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(),
                        Map.of("message", exception.getMessage()),
                        HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(DuplicateRoleException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateRoleException(DuplicateRoleException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(),
                        Map.of("message", exception.getMessage()),
                        HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleServerErrorException(ServerErrorException exception) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(
                        Instant.now(),
                        Map.of("message", exception.getMessage()),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }
}
