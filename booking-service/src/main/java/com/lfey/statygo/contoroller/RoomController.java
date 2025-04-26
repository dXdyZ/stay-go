package com.lfey.statygo.contoroller;

import com.lfey.statygo.dto.CreateRoom;
import com.lfey.statygo.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PatchMapping("/add/{hotelId}")
    public void addRoomsToHotel(@PathVariable Long hotelId,
                                @Valid @RequestBody List<CreateRoom> createRooms) {
        roomService.addRoomsToHotel(hotelId, createRooms);
    }
}
