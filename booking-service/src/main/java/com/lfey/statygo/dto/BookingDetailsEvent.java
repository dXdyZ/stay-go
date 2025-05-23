package com.lfey.statygo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailsEvent {
    private Long bookingId;
    private String hotelName;
    private String hotelAddress;
    private String startDate;
    private String endDate;
    private String roomType;
    private String username;
    private Double totalPrice;
    private String bookingStatus;
    private Integer reservedRooms;
}
