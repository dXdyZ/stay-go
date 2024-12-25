package com.staygo.enity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO implements Serializable {
    private String name;
    private Integer grade;
    private String country;
    private String city;
    private String street;
    private String allPrice;
    private String houseNumber;
    private List<RoomDTO> rooms;
    private List<CommentsDTO> comments;
    private Map<String, Integer> weather;
}
