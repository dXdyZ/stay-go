package com.staygo.enity.DTO;

import com.staygo.enity.hotel.RoomData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private String roomName;
    private String roomStatus;
    private BigDecimal price;
    private String prestige;
    private HotelDTO hotelDTO;
    private List<RoomData> roomData;
}
