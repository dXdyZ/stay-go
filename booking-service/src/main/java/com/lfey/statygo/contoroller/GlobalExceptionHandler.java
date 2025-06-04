package com.lfey.statygo.contoroller;

import com.lfey.statygo.exception.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
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
                       "code": "400" 
                    }
                    """
    )
    public record ErrorResponse(
            Instant timestamp,
            Map<String, String> error,
            Integer code
    ) {};

    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleHotelNotFoundException(HotelNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        Instant.now(), Map.of("message", exception.getMessage()), HttpStatus.NOT_FOUND.value()
                ));
    }

    @ExceptionHandler(DuplicateRoomException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateRoomException(DuplicateRoomException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), Map.of("message", exception.getMessage()), HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(NoMatchingRoomsException.class)
    public ResponseEntity<ErrorResponse> handleNoMatchingRoomsException(NoMatchingRoomsException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), Map.of("message", exception.getMessage()), HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(NoRoomsAvailableException.class)
    public ResponseEntity<ErrorResponse> handleNoRoomsAvailableException(NoRoomsAvailableException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), Map.of("message", exception.getMessage()), HttpStatus.CONFLICT.value()
                ));
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDateException(InvalidDateException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), Map.of("message", exception.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY.value()
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), Map.of("message", exception.getMessage()), HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(ValidateFileException.class)
    public ResponseEntity<ErrorResponse> handleValidationFileException(ValidateFileException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), Map.of("message", exception.getMessage()), HttpStatus.BAD_REQUEST.value()
                ));
    }
}





