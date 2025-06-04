package com.example.staygoclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRoomDto {
    private Long hotelId;
    private String startDate;
    private String endDate;
    private String roomType;
    private Integer guests;
    private Integer numberOfRooms;
}
