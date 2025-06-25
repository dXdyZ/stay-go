package com.lfey.authservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lfey.authservice.exception.*;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Schema(description = "Error response",
            example = """
                    {
                       "timestamp": "2025-05-04 04:02:20.626585",
                       "message": {
                           "message": "Error message"
                       },
                       "code": 400 
                    }
                    """
    )
    public record ErrorResponse(
            Instant timestamp,
            Map<String, String> error,
            Integer code
    ) {
        public String toJson() throws JsonProcessingException {
            return new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .writeValueAsString(this);
        }
    }

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
                        Instant.now(), Map.of("message", ex.getMessage()), HttpStatus.NOT_FOUND.value()
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

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRefreshTokenException(InvalidRefreshTokenException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(
                        Instant.now(),
                        Map.of("message", exception.getMessage()),
                        HttpStatus.UNAUTHORIZED.value()
                ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        Instant.now(),
                        Map.of("message", exception.getMessage()),
                        HttpStatus.NOT_FOUND.value()
                ));
    }
}
