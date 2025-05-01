package com.lfey.statygo.service;

import com.lfey.statygo.component.CustomDateFormatter;
import com.lfey.statygo.entity.Room;
import com.lfey.statygo.entity.RoomType;
import com.lfey.statygo.exception.NoRoomsAvailableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class RoomAvailabilityService {
    private final RoomService roomService;

    public RoomAvailabilityService(RoomService roomService) {
        this.roomService = roomService;
    }

    @Transactional
    public List<Room> getFreeRooms(Long hotelId, String startDate, String endDate, String roomType,
                                             Integer guests, Integer numberOfRooms) throws NoRoomsAvailableException{
        CustomDateFormatter.dateVerification(startDate, endDate);
        List<Room> roomAvailableIds = roomService.getAvailableRoom(
                hotelId, guests,
                CustomDateFormatter.localDateFormatter(startDate),
                CustomDateFormatter.localDateFormatter(endDate),
                RoomType.valueOf(roomType)
        );

        if (roomAvailableIds.isEmpty())
            throw new NoRoomsAvailableException("No available rooms of this type on the specified date");

        numberOfRooms = Objects.requireNonNullElse(numberOfRooms, Integer.MAX_VALUE);

        return roomAvailableIds.stream()
                .limit(numberOfRooms)
                .toList();
    }
}
