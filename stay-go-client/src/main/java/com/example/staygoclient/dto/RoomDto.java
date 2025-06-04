package com.example.staygoclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private String roomType;
    private Integer capacity;
    private Double totalPrice;
    private String bedType;
    private Double roomSize;
}

