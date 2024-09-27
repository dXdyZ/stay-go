package com.staygo.enity.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private String roomName;
    private String roomStatus;
    private BigDecimal price;
    private String prestige;
    private HotelDTO hotelData;
}
