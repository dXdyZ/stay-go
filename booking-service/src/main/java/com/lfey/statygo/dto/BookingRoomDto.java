package com.lfey.statygo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Entity for booking room by user",
        example = """
                {
                    "hotelId": 1, 
                    "startDate": "2025-03-14",
                    "endDate": "2025-03-15",
                    "roomType": "STANDARD",
                    "guests": 1,
                     
                }
                """
)
public class BookingRoomDto {

    private Long hotelId;

    @NotBlank(message = "Start date is required.")
    private String startDate;

    @NotBlank(message = "End date is required.")
    private String endDate;

    @NotBlank(message = "Room type is required.")
    private String roomType;

    @NotBlank(message = "Number of guests is required.")
    private Integer guests;

    @NotBlank(message = "Number of rooms is required.")
    private Integer numberOfRooms;
}






