package com.lfey.statygo.service;

import com.lfey.statygo.dto.CreateRoom;
import com.lfey.statygo.entity.Hotel;
import com.lfey.statygo.entity.Room;
import com.lfey.statygo.entity.RoomType;
import com.lfey.statygo.exception.DuplicateRoomException;
import com.lfey.statygo.exception.HotelNotFoundException;
import com.lfey.statygo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final HotelService hotelService;

    @Autowired
    public RoomService(RoomRepository roomRepository, HotelService hotelService) {
        this.roomRepository = roomRepository;
        this.hotelService = hotelService;
    }

    @Transactional
    public List<Room> getRoomByHotelIdAndCapacityAndRoomType(Long hotelId, Integer capacity, RoomType roomType) {
        return roomRepository.findRoomsByHotelIdAndRoomTypeAndCapacity(hotelId, capacity, roomType);
    }

    @Transactional
    public void addRoomsToHotel(Long hotelId, List<CreateRoom> createRooms) throws DuplicateRoomException, HotelNotFoundException {
        Hotel hotel = hotelService.getHotelById(hotelId);
        if (hotel.getRoom() != null && !hotel.getRoom().isEmpty()) existsRoomByHotel(hotel.getRoom(), createRooms);
        hotel.getRoom().addAll(createRooms.stream()
                .map(createRoom -> {
                    return Room.builder()
                            .capacity(createRoom.getCapacity())
                            .roomType(createRoom.getRoomType())
                            .description(createRoom.getDescription())
                            .pricePerDay(createRoom.getPricePerDay())
                            .build();
                })
                .toList());
        hotelService.saveHotel(hotel);
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
