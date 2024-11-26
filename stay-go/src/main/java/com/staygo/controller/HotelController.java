package com.staygo.controller;

import com.staygo.enity.DTO.HotelDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/findHotel/{country}/{city}")
    public ResponseEntity<?> findHotelByCityAndGradeAndArmoredDate(@PathVariable("city") String city,
                                                                   @PathVariable("country") String country,
                                                                   @RequestParam(name = "dateArmored") String dateArmored,
                                                                   @RequestParam(name = "departureDate") String departureDate,
                                                                   @RequestParam(name = "grade", required = false) Integer grade,
                                                                   Principal principal) {
        List<HotelDTO> hotelDTOS = hotelService.findHotelForSendMessage(country, city, dateArmored,
                departureDate, grade, principal);
        if (hotelDTOS != null) {
            return ResponseEntity.ok(hotelDTOS);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateData")
    public ResponseEntity<?> updateDataHotel(@RequestBody HotelDTO hotelDTO) {
        try {
            return ResponseEntity.ok(hotelService.updateToDataHotel(hotelDTO));
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addedRoom/{hotelName}/{country}/{city}/{street}")
    public ResponseEntity<?> addedRoomToHotel(@RequestPart List<Room> room,
                                              @RequestPart List<MultipartFile> roomFile,
                                              Principal principal,
                                              @PathVariable("street") String street,
                                              @PathVariable("country") String country,
                                              @PathVariable("city") String city,
                                              @PathVariable("hotelName") String hotelName) {
        return roomService.addedARoomToTheHotel(principal, street, room, roomFile, city, country, hotelName);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllHotelUser(Principal principal) {
        return hotelService.getAllHotelUsers(principal);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delAll() {
        hotelService.deleteAllHotel();
    }

}
