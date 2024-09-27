package com.staygo.controller;

import com.staygo.service.hotel_ser.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/modifyName")
    public ResponseEntity<?> modifyRoomName(@RequestParam(name = "oldName") String oldName,
                                            @RequestParam(name = "newName") String newName) {
        return roomService.modifyRoomName(oldName, newName);
    }

//    @PostMapping("/addedPhoto")
//    public ResponseEntity<?> addedPhotoToRoom(@RequestPart("roomPhotos")List<MultipartFile> roomPhotos) {
//
//    }
}
