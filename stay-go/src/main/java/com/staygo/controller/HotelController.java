package com.staygo.controller;

import com.staygo.enity.hotel.Hotel;
import com.staygo.enity.hotel.Room;
import com.staygo.service.hotel_ser.HotelService;
import com.staygo.service.hotel_ser.RoomService;
import jakarta.servlet.annotation.HttpConstraint;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {
    private final HotelService hotelService;
    private final RoomService roomService;

    @Autowired
    public HotelController(HotelService hotelService,
                           RoomService roomService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createHotel(@RequestPart("hotel") String hotel,
                                        @RequestPart("hotelFiles") List<MultipartFile> hotelFiles,
                                        Principal principal) {
        return hotelService.createdHotel(hotel, hotelFiles, principal);
    }

    @PostMapping("/addedRoom/{street}")
    public ResponseEntity<?> addedRoomToHotel(@RequestPart List<Room> room,
                                              @RequestPart List<MultipartFile> roomFile,
                                              Principal principal,
                                              @PathVariable("street") String street) {
        return roomService.addedARoomToTheHotel(principal, street, room, roomFile);
    }

    @GetMapping("/delete")
    public void delAll() {
        hotelService.deleteAllHotel();
    }

    @GetMapping("/all")
    public List<Hotel> delete() {
        return hotelService.allHotel();
    }
}
