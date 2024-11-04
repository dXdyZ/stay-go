package com.staygo.service.hotel_ser;

import com.staygo.enity.DTO.rabbit.ArmoredRoomDTO;
import com.staygo.enity.hotel.ArmoredRoom;
import com.staygo.enity.hotel.Hotel;
import com.staygo.repository.hotel_repo.ArmoredRoomRepository;
import com.staygo.service.rabbit.RabbitBookingMessage;
import com.staygo.service.user_ser.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class ArmoredRoomService {
    private final ArmoredRoomRepository armoredRoomRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final RabbitBookingMessage rabbitBookingMessage;

    @Autowired
    public ArmoredRoomService(ArmoredRoomRepository armoredRoomRepository,
                              UserService userService, RoomService roomService, RabbitBookingMessage rabbitBookingMessage) {
        this.armoredRoomRepository = armoredRoomRepository;
        this.userService = userService;
        this.roomService = roomService;
        this.rabbitBookingMessage = rabbitBookingMessage;
    }

    @Deprecated
    public boolean findByArmoredData(String armoredData, String departureData, String city) {
        List<ArmoredRoom> armoredRooms = armoredRoomRepository.findByDateArmoredAndDepartureDateAndRoom_Hotel_Address_City(armoredData, departureData, city);
        return armoredData != null && !armoredData.isEmpty();
    }

    @Transactional
    public ResponseEntity<?> armoredHotel(String city, String street ,String hotelName, String armoredDate,
                                          String departureDate, Principal principal, String prestige) {

        ArmoredRoom armoredRoom = ArmoredRoom.builder()
                .createDate(new Date())
                .dateArmored(armoredDate)
                .departureDate(departureDate)
                .room(roomService.findNotArmoredRoom(street, armoredDate, departureDate, city, hotelName, prestige))
                .users(userService.findByName(principal.getName()).get())
                .build();

        armoredRoomRepository.save(armoredRoom);


        rabbitBookingMessage.sendDataBooking(createArmoredDTO(armoredRoom, street, city, hotelName));

        return ResponseEntity.ok().build();
    }

    public ArmoredRoomDTO createArmoredDTO(ArmoredRoom armoredRoom, String street, String city, String hotelName) {
        Hotel hotel = roomService.findHotelForBookingRoom(street, city, hotelName);
        return ArmoredRoomDTO.builder()
                .username(armoredRoom.getUsers().getUsername())
                .email(armoredRoom.getUsers().getEmail())
                .createDate(armoredRoom.getCreateDate())
                .roomNumber(armoredRoom.getRoom().getRoomName())
                .prestige(armoredRoom.getRoom().getPrestige())
                .dateArmored(armoredRoom.getDateArmored())
                .departureDate(armoredRoom.getDepartureDate())
                .hotelName(hotel.getName())
                .city(hotel.getAddress().getCity())
                .country(hotel.getAddress().getCountry())
                .street(hotel.getAddress().getStreet())
                .houseNumber(hotel.getAddress().getNumberHouse())
                .build();
    }
}
