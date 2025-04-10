package com.lfey.statygo.contoroller;

import com.lfey.statygo.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {
    public record ErrorResponse(
            Instant createAt,
            String message,
            Integer statusCode
    ) {};

    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleHotelNotFoundException(HotelNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        Instant.now(), exception.getMessage(), HttpStatus.NOT_FOUND.value()
                ));
    }

    @ExceptionHandler(DuplicateRoomException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateRoomException(DuplicateRoomException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), exception.getMessage(), HttpStatus.NOT_FOUND.value()
                ));
    }

    @ExceptionHandler(NoMatchingRoomsException.class)
    public ResponseEntity<ErrorResponse> handleNoMatchingRoomsException(NoMatchingRoomsException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), exception.getMessage(), HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(NoRoomsAvailableException.class)
    public ResponseEntity<ErrorResponse> handleNoRoomsAvailableException(NoRoomsAvailableException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), exception.getMessage(), HttpStatus.CONFLICT.value()
                ));
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDateException(InvalidDateException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        Instant.now(), exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value()
                ));
    }
}





