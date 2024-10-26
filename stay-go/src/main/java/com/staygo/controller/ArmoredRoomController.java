package com.staygo.controller;

import com.staygo.service.hotel_ser.ArmoredRoomService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/armored")
public class ArmoredRoomController {
    private final ArmoredRoomService armoredRoomService;

    @Autowired
    public ArmoredRoomController(ArmoredRoomService armoredRoomService) {
        this.armoredRoomService = armoredRoomService;
    }

    @PostMapping
    public ResponseEntity<?> armoredRoom(@RequestBody ArmoredData armoredData, Principal principal) {
        return armoredRoomService.armoredHotel(armoredData.getCity(), armoredData.getHotelName(),
                armoredData.getArmoredDate(), armoredData.getDepartureDate(), principal, armoredData.getPrestige());
    }
}

@Data
@Builder
@AllArgsConstructor
class ArmoredData {
    private String city;
    private String hotelName;
    private String armoredDate;
    private String departureDate;
    private String prestige;
}
