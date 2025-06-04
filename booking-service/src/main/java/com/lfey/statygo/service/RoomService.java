package com.lfey.statygo.service;

import com.lfey.statygo.dto.CreateRoomDto;
import com.lfey.statygo.entity.Hotel;
import com.lfey.statygo.entity.Room;
import com.lfey.statygo.entity.RoomType;
import com.lfey.statygo.exception.DuplicateRoomException;
import com.lfey.statygo.exception.HotelNotFoundException;
import com.lfey.statygo.repository.HotelRepository;
import com.lfey.statygo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public List<Room> getAvailableRoom(
            Long hotelId, Integer capacity, LocalDate startDate, LocalDate endDate, RoomType roomType) {

        return roomRepository.findAvailableRoomIds(hotelId, capacity, startDate, endDate, roomType);
    }

    @Transactional
    public void addRoomsToHotel(Long hotelId, List<CreateRoomDto> createRoomDtos) throws DuplicateRoomException,
            HotelNotFoundException, IllegalArgumentException{

        if (createRoomDtos == null || createRoomDtos.isEmpty())
            throw new IllegalArgumentException("List of rooms cannot be empty.");

        if (!hotelRepository.existsById(hotelId)) throw new HotelNotFoundException(
                "Hotel by id: %s not found".formatted(hotelId));

        if (createRoomDtos.stream()
                .map(CreateRoomDto::getNumber)
                .collect(Collectors.toSet()).size() != createRoomDtos.size())
            throw new DuplicateRoomException("Duplicates number in added rooms");

        if (!roomRepository.findNumbersByHotelIdAndNumberIn(
                hotelId,
                createRoomDtos.stream()
                        .map(CreateRoomDto::getNumber)
                        .collect(Collectors.toSet())).isEmpty())
            throw new DuplicateRoomException("The rooms to be added already exist");

        roomRepository.saveAll(createRoomDtos.stream()
                .map(createRoomDto -> {
                    return Room.builder()
                            .hotel(Hotel.builder().id(hotelId).build())
                            .capacity(createRoomDto.getCapacity())
                            .roomType(createRoomDto.getRoomType())
                            .bedType(createRoomDto.getBedType())
                            .description(createRoomDto.getDescription())
                            .pricePerDay(createRoomDto.getPricePerDay())
                            .number(createRoomDto.getNumber())
                            .build();
                })
                .toList());
    }
}
