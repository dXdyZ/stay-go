package com.staygo.enity.DTO;

import com.staygo.enity.address.Address;
import com.staygo.enity.hotel.Comments;
import com.staygo.enity.hotel.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO implements Serializable {
    private String name;
    private Integer grade;
    private Address address;
    private List<Room> rooms;
    private List<Comments> comments;
}
