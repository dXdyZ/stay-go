package com.lfey.statygo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@Schema(
        description = "Booking entity",
        example = """
                {
                    "id": 1,
                    "hotelName": "Hotel",
                    "roomNumber": 12,
                    "bookingStatus": "CONFIRMED",
                    "startDate": "2025-03-14",
                    "endDate": "2025-03-15",
                    "totalPrice": 1234.4,
                    "username": "user",
                    "guests": 2
                }
                """
)
public class BookingDto {
    @Schema(description = "Booking id", example = "1")
    private Long id;
    @Schema(description = "Hotel name", example = "Hotel")
    private String hotelName;
    @Schema(description = "The number of the room the user is checking into", example = "12")
    private int roomNumber;
    @Schema(description = "Booking status", example = "CONFIRMED")
    private String bookingStatus;
    @Schema(description = "Check-in date", example = "2025-03-14")
    private LocalDate startDate;
    @Schema(description = "Departure date", example = "2025-03-15")
    private LocalDate endDate;
    @Schema(description = "Total price", example = "1234.4")
    private Double totalPrice;
    @Schema(description = "The name of the user who booked the room", example = "user")
    private String username;
    @Schema(description = "Number of guests", example = "2")
    private Integer guests;
}
