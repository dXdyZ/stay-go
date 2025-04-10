package com.lfey.statygo.dto;

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
    private String startDate;
    private String endDate;
    private String roomType;
    private Integer guests;
    private Integer numberOfRooms;
}
