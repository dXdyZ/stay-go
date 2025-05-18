package com.example.staygoclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {
    private Long hotelId;
    private Double grade;
    private Integer stars;
    private String name;
    private String description;
    private String address;

    private List<RoomDto> roomDto = new ArrayList<>();
}

