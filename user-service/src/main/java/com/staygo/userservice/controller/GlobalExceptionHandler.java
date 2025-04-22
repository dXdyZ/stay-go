package com.staygo.userservice.controller;

import com.staygo.userservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

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

    @ExceptionHandler(DuplicateRoleException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateRoleException(DuplicateRoleException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .errorCode(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(FeignCustomServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleFeignCustomException(FeignCustomServerErrorException ex) {
        ErrorResponse error = ex.getErrorResponse();
        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getErrorCode()));
    }
}
