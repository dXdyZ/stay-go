package com.staygo.enity.DTO;

import com.staygo.enity.Address;
import com.staygo.enity.hotel.Comments;
import com.staygo.enity.hotel.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {
    private String name;
    private Integer grade;
    private Address address;
    private List<Room> rooms;
    private List<Comments> comments;
}
