package com.lfey.authservice.controller;

import com.lfey.authservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCode(InvalidCodeException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), ex.getMessage(), HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(UserCacheDataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserRegNotFound(UserCacheDataNotFoundException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), ex.getMessage(), HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserException(DuplicateUserException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), ex.getMessage(), HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(DuplicateRoleException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateRoleException(DuplicateRoleException ex) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(
                        Instant.now(), ex.getMessage(), HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleServerErrorException(ServerErrorException ex) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(
                        Instant.now(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationFailedException(AuthenticationFailedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(
                        Instant.now(), ex.getMessage(), HttpStatus.UNAUTHORIZED.value()
                ));
    }
}
