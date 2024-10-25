package com.staygo.service.hotel_ser;

import com.staygo.enity.hotel.ArmoredRoom;
import com.staygo.repository.hotel_repo.ArmoredRoomRepository;
import com.staygo.repository.hotel_repo.RoomRepository;
import com.staygo.service.user_ser.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class ArmoredRoomService {
    private final ArmoredRoomRepository armoredRoomRepository;
    private final UserService userService;
    private final RoomService roomService;

    @Autowired
    public ArmoredRoomService(ArmoredRoomRepository armoredRoomRepository,
                              UserService userService, RoomService roomService) {
        this.armoredRoomRepository = armoredRoomRepository;
        this.userService = userService;
        this.roomService = roomService;
    }

    @Deprecated
    public boolean findByArmoredData(String armoredData, String departureData, String city) {
        List<ArmoredRoom> armoredRooms = armoredRoomRepository.findByDateArmoredAndDepartureDateAndRoom_Hotel_Address_City(armoredData, departureData, city);
        return armoredData != null && !armoredData.isEmpty();
    }

    public ResponseEntity<?> armoredHotel(String city, String hotelName, String armoredDate,
                                          String departureDate, Principal principal, String prestige) {
        armoredRoomRepository.save(ArmoredRoom.builder()
                .createDate(new Date())
                .dateArmored(armoredDate)
                .departureDate(departureDate)
                .room(roomService.findNotArmoredRoom(armoredDate, departureDate, city, hotelName, prestige))
                .users(userService.findByName(principal.getName()).get())
                .build());
        return ResponseEntity.ok().build();
    }
}
