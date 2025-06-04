package com.lfey.statygo.contoroller;

import com.lfey.statygo.dto.CreateRoomDto;
import com.lfey.statygo.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/{hotelId}")
    public void addRoomsToHotel(@PathVariable Long hotelId,
                                @Valid @RequestBody List<CreateRoomDto> createRoomDto) {
        roomService.addRoomsToHotel(hotelId, createRoomDto);
    }
}
