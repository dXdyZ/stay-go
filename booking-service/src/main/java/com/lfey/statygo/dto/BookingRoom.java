package com.lfey.statygo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRoom {

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






