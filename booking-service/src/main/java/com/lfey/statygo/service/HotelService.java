package com.lfey.statygo.service;

import com.lfey.statygo.component.CustomPageable;
import com.lfey.statygo.dto.CreateHotel;
import com.lfey.statygo.dto.CreateRoom;
import com.lfey.statygo.entity.Address;
import com.lfey.statygo.entity.Hotel;
import com.lfey.statygo.entity.Room;
import com.lfey.statygo.exception.DuplicateRoomException;
import com.lfey.statygo.exception.HotelNotFoundException;
import com.lfey.statygo.repository.HotelRepository;
import com.lfey.statygo.repository.specification.HotelSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final RoomService roomService;

    @Autowired
    public HotelService(HotelRepository hotelRepository, RoomService roomService) {
        this.hotelRepository = hotelRepository;
        this.roomService = roomService;
    }

    @Transactional
    public Hotel saveHotel(CreateHotel createHotel) {
        return hotelRepository.save(Hotel.builder()
                        .stars(createHotel.getStars())
                        .name(createHotel.getName())
                        .address(Address.builder()
                                .city(createHotel.getCity())
                                .country(createHotel.getCountry())
                                .houseNumber(createHotel.getHouseNumber())
                                .street(createHotel.getStreet())
                                .build())
                .build());
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id).orElseThrow(
                () -> new HotelNotFoundException(id)
        );
    }

    @Transactional
    public void addRooms(Long hotelId, List<CreateRoom> createRooms) throws DuplicateRoomException {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new HotelNotFoundException(hotelId));
        if (hotel.getRoom() != null && !hotel.getRoom().isEmpty()) roomService.existsRoomByHotel(hotel.getRoom(), createRooms);
        hotel.setRoom(createRooms.stream()
                .map(createRoom -> {
                    return Room.builder()
                            .capacity(createRoom.getCapacity())
                            .roomType(createRoom.getRoomType())
                            .description(createRoom.getDescription())
                            .pricePerDay(createRoom.getPricePerDay())
                            .build();
                })
                .toList());
    }

    public List<Hotel> getByHotelByName(String name, int page) {
        return hotelRepository.findByName(name, CustomPageable.getPageable(page));
    }

    @Transactional
    public void deleteHotelById(Long id) throws HotelNotFoundException{
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException(id));
        hotelRepository.delete(hotel);
    }

    public Page<Hotel> findHotel(Integer stars, String country, String city,
                                 String street, String houseNumber, int page) {
        Specification<Hotel> spec = Specification.where(null);
        if (stars != null) spec = spec.and(HotelSpecification.hasStars(stars));
        if (country != null) spec = spec.and(HotelSpecification.hasCountry(country));
        if (street != null) spec = spec.and(HotelSpecification.hasStreet(street));
        if (city != null) spec = spec.and(HotelSpecification.hasCity(city));
        if (houseNumber != null) spec = spec.and(HotelSpecification.hasHouseNumber(houseNumber));

        return hotelRepository.findAll(spec, CustomPageable.getPageable(page));
    }
}
