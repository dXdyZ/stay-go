package com.lfey.statygo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Booking history",
        example = """
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
                """
)
public class BookingHistoryDto implements Serializable {
    @Schema(description = "Unique identifier of the booking", example = "12345")
    private Long bookingId;
    @Schema(description = "Unique identifier of the hotel", example = "789")
    private Long hotelId;
    @Schema(description = "Name of the hotel", example = "Grand Paradise Resort")
    private String hotelName;
    @Schema(description = "Star rating of the hotel (1-5)", example = "5")
    private Integer hotelStars;
    @Schema(
            description = "Type of the hotel",
            example = "LUXURY",
            allowableValues = {"HOTEL", "APARTMENTS", "VILLA", "HOUSE"}
    )
    private String hotelType;
    @Schema(description = "URL of the main hotel photo", example = "https://example.com/uploads/grand-paradise.jpg")
    private String mainPhotoUrl;
    @Schema(description = "Check-in date in ISO format (YYYY-MM-DD)", example = "2023-12-15")
    private LocalDate startDate;
    @Schema(description = "Check-out date in ISO format (YYYY-MM-DD)", example = "2023-12-22")
    private LocalDate endDate;
    @Schema(description = "Total price of the booking", example = "1200.50")
    private Double totalPrice;
    @Schema(description = "Timestamp when booking was created (ISO-8601 with UTC)",
            example = "2023-10-05T14:30:00Z")
    private Instant createDate;
    @Schema(description = "Room number",example = "305")
    private Integer roomNumber;
    @Schema(description = "Detailed description of the room", example = "Deluxe King Room with Ocean View")
    private String roomDescription;
    @Schema(
            description = "Type of bed in the room",
            example = "KING",
            allowableValues = {"SINGLE", "DOUBLE", "QUEEN", "KING", "TWO_TIRE"}
    )
    private String bedType;
}
