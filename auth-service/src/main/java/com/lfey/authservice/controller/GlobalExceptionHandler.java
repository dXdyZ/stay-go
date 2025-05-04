package com.lfey.authservice.controller;

import com.lfey.authservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorResponse(
            Instant timestamp,
            Map<String, String> message,
            Integer code
    ) {}

    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCode(InvalidCodeException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), Map.of("message", ex.getMessage()), HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(UserCacheDataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserRegNotFound(UserCacheDataNotFoundException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), Map.of("message", ex.getMessage()), HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserException(DuplicateUserException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), Map.of("message", ex.getMessage()), HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(DuplicateRoleException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateRoleException(DuplicateRoleException ex) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(
                        Instant.now(), Map.of("message", ex.getMessage()), HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleServerErrorException(ServerErrorException ex) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(
                        Instant.now(),  Map.of("message", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationFailedException(AuthenticationFailedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(
                        Instant.now(), Map.of("message", ex.getMessage()), HttpStatus.UNAUTHORIZED.value()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(),
                        exception.getBindingResult().getFieldErrors().stream()
                                .collect(Collectors.toMap(
                                        FieldError::getField,
                                        error -> error.getDefaultMessage() != null ?
                                                error.getDefaultMessage() : "Invalid value"
                                )),
                        HttpStatus.BAD_REQUEST.value()
                ));
    }
}
