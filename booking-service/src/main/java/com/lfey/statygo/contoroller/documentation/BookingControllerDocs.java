package com.lfey.statygo.contoroller.documentation;

import com.lfey.statygo.component.PageResponse;
import com.lfey.statygo.contoroller.GlobalExceptionHandler;
import com.lfey.statygo.dto.BookingDto;
import com.lfey.statygo.dto.BookingHistoryDto;
import com.lfey.statygo.dto.BookingRoomDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@OpenAPIDefinition
public interface BookingControllerDocs {

    String USER_PUBLIC_ID = "X-User-PublicId";


    @Operation(
            summary = "Booking room",
            description = "Booking a room by the user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = BookingRoomDto.class))
            )
    )
    @PostMapping
    void booingRoom(@Valid @RequestBody BookingRoomDto bookingRoomDto,
                           @RequestHeader(USER_PUBLIC_ID) UUID publicId);


    @Operation(
            summary = "Get booking by id",
            description = "Getting booking by id",
            responses = @ApiResponse(responseCode = "200", description = "Success response",
                    content = @Content(schema = @Schema(implementation = BookingDto.class))
            )
    )
    @ApiResponse(responseCode = "404", description = "Hotel not found",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                                name = "Hotel not found",
                                value = """
                                        {
                                            "timestamp": "2024-07-15T14:30:45Z",
                                            "error": {
                                                "message": "Booking by id: 1 not found"
                                            },
                                            "code": 404
                                        }
                                        """
                        )
                    }
            )
    )
    @GetMapping("/{id}")
    ResponseEntity<BookingDto> getBookingById(
            @Parameter(description = "Booking id", example = "1", required = true)
            @PathVariable Long id);


    @Operation(
            summary = "Get bookings for the period",
            description = "Get all hotel bookings for the specified period",
            responses = @ApiResponse(responseCode = "200", description = "Success response",
                    content = @Content(schema = @Schema(implementation = BookingDto.class),
                            examples = {
                                @ExampleObject(
                                        name = "Booking page response",
                                        value = """
                                                {
                                                    "content": [
                                                        {
                                                            "id": 1,
                                                            "hotelName": "Hotel",
                                                            "roomNumber": 12,
                                                            "bookingStatus": "CONFIRMED",
                                                            "startDate": "2025-03-14",
                                                            "endDate": "2025-03-15",
                                                            "totalPrice": 1234.4,
                                                            "username": "user"
                                                        }
                                                    ],
                                                    "currentPage": 0,
                                                    "totalPages": 5,
                                                    "totalItems": 50
                                                }
                                                """
                                )
                            }
                    )
            )
    )
    @GetMapping("/hotels/{hotelId}/period")
    ResponseEntity<PageResponse<BookingDto>> getBookingByPeriod(
            @Parameter(description = "Hotel id", example = "1", required = true)
            @PathVariable Long hotelId,
            @Parameter(description = "Starting date", example = "2025-03-14", required = true)
            @RequestParam String startDate,
            @Parameter(description = "End date", example = "2025-03-15")
            @RequestParam(required = false) String endDate);


    @Operation(
        summary = "Get all bookings",
        description = "Get all hotel reservations for the entire period of its existence",
        responses = @ApiResponse(responseCode = "200", description = "Success response",
                content = @Content(schema = @Schema(implementation = BookingDto.class),
                        examples = {
                            @ExampleObject(
                                    name = "Booking page response",
                                    value = """
                                            {
                                                "content": [
                                                    {
                                                        "id": 1,
                                                        "hotelName": "Hotel",
                                                        "roomNumber": 12,
                                                        "bookingStatus": "CONFIRMED",
                                                        "startDate": "2025-03-14",
                                                        "endDate": "2025-03-15",
                                                        "totalPrice": 1234.4,
                                                        "username": "user"
                                                    }
                                                ],
                                                "currentPage": 0,
                                                "totalPages": 5,
                                                "totalItems": 50
                                            }
                                            """
                            )
                        }
                )
        )
    )
    @GetMapping("/hotels/{hotelId}")
    ResponseEntity<PageResponse<BookingDto>> getAllBookingByHotel(
        @Parameter(description = "Hotel id", example = "1", required = true)
        @PathVariable Long hotelId);


    @Operation(
            summary = "Get user booking history",
            description = "Get user booking history",
            responses = @ApiResponse(responseCode = "200", description = "Success response",
                    content = @Content(schema = @Schema(implementation = BookingHistoryDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Booking page response",
                                            value = """
                                            {
                                                "content": [
                                                    {
                                                        "bookingId": 12345,
                                                        "hotelId": 789,
                                                        "hotelName": "Grand Paradise Resort",
                                                        "hotelStars": 5,
                                                        "hotelType": "HOTEL",
                                                        "mainPhotoUrl": "https://example.com/uploads/grand-paradise.jpg",
                                                        "startDate": "2023-12-15",
                                                        "endDate": "2023-12-22",
                                                        "totalPrice": 1200.50,
                                                        "createDate": "2023-10-05T14:30:00Z",
                                                        "roomNumber": 305,
                                                        "roomDescription": "Deluxe King Room with Ocean View",
                                                        "bedType": "KING
                                                    }
                                                ],
                                                "currentPage": 0,
                                                "totalPages": 5,
                                                "totalItems": 50
                                            }
                                            """
                                    )
                            }
                    )
            )
    )
    @GetMapping("/history")
    ResponseEntity<PageResponse<BookingHistoryDto>> getUserBookingHistory(
            @RequestHeader(USER_PUBLIC_ID) String username,
            @RequestParam(required = false, defaultValue = "0") int page);
}
