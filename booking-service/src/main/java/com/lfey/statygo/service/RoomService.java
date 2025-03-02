package com.lfey.statygo.service;

import com.lfey.statygo.dto.CreateRoom;
import com.lfey.statygo.entity.Room;
import com.lfey.statygo.exception.DuplicateRoomException;
import com.lfey.statygo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Проверка на уникальность добавляемых комнат с уже созданными
     */
    public void existsRoomByHotel(List<Room> rooms, List<CreateRoom> roomDto) throws DuplicateRoomException{
        Set<CreateRoom> uniqueNumber = new HashSet<>();
        List<CreateRoom> duplicate = roomDto.stream()
                .filter(n -> !uniqueNumber.add(n))
                .toList();
        for (CreateRoom createRoom : roomDto) {
            for (Room room : rooms) {
                if (createRoom.getNumber().equals(room.getNumber())) {
                    throw new DuplicateRoomException("The rooms to be added already exist");
                }
            }
        }
        if (!duplicate.isEmpty()) {
            throw new DuplicateRoomException("Duplicates number in added rooms");
        }
    }


}
