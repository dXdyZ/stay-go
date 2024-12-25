package com.staygo.controller;

import com.staygo.service.hotel_ser.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/modifyName/{hotelName}/{country}/{city}/{street}")
    public ResponseEntity<?> modifyRoomName(@RequestParam(name = "oldName") String oldName,
                                            @RequestParam(name = "newName") String newName,
                                            @PathVariable("hotelName") String hotelName,
                                            @PathVariable("country") String country,
                                            @PathVariable("city") String city,
                                            @PathVariable("street") String street, Principal principal) {
        return roomService.modifyRoomName(hotelName, city, country, street, principal.getName(), oldName, newName);
    }
}
