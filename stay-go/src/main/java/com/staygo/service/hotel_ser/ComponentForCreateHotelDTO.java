package com.staygo.service.hotel_ser;

import com.staygo.enity.DTO.HotelDTO;
import com.staygo.enity.DTO.RoomDTO;
import com.staygo.enity.hotel.Hotel;
import com.staygo.enity.hotel.Room;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ComponentForCreateHotelDTO {

    public List<RoomDTO> sampleFirstByPrestige(List<Room> rooms) {
        List<Room> roomList = rooms.stream()
                .collect(Collectors.toMap(
                        Room::getPrestige,
                        room -> room,
                        (existing, replacement) -> existing
                ))
                .values().stream()
                .toList();
        return roomList.stream().map(room -> new RoomDTO(room.getRoomName(),
                room.getRoomStatus(), room.getPrice(), room.getPrestige())).toList();
    }

    public HotelDTO getHotelDTO(Hotel hotel) {
        return HotelDTO.builder()
                .name(hotel.getName())
                .grade(hotel.getGrade())
                .country(hotel.getAddress().getCountry())
                .city(hotel.getAddress().getCity())
                .street(hotel.getAddress().getStreet())
                .houseNumber(hotel.getAddress().getNumberHouse())
                .rooms(sampleFirstByPrestige(hotel.getRooms()))
                .build();
    }
}
