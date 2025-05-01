package com.lfey.statygo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingDto {
    private Long id;
    private String hotelName;
    private int roomNumber;
    private String bookingStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalPrice;
    private String username;

}
