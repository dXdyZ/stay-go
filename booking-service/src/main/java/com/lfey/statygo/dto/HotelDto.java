package com.lfey.statygo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {
    private Long hotelId;
    private Double grade;
    private Integer stars;
    private String name;
    private String description;
    private String address;
    @Builder.Default
    private List<RoomDto> roomDto = new ArrayList<>();
}












