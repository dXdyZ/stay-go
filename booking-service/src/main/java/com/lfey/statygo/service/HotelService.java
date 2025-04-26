package com.lfey.statygo.service;

import com.lfey.statygo.component.CustomPageable;
import com.lfey.statygo.component.factory.HotelDtoFactory;
import com.lfey.statygo.component.factory.RoomDtoFactory;
import com.lfey.statygo.dto.CreateHotel;
import com.lfey.statygo.dto.HotelDto;
import com.lfey.statygo.entity.Address;
import com.lfey.statygo.entity.Hotel;
import com.lfey.statygo.entity.Room;
import com.lfey.statygo.exception.HotelNotFoundException;
import com.lfey.statygo.repository.HotelRepository;
import com.lfey.statygo.repository.specification.HotelSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final RoomAvailabilityService roomAvailabilityService;
    private final HotelDtoFactory hotelDtoFactory;
    private final RoomDtoFactory roomDtoFactory;


    @Autowired
    public HotelService(HotelRepository hotelRepository, RoomAvailabilityService roomAvailabilityService,
                        HotelDtoFactory hotelDtoFactory, RoomDtoFactory roomDtoFactory) {
        this.hotelRepository = hotelRepository;
        this.roomAvailabilityService = roomAvailabilityService;
        this.hotelDtoFactory = hotelDtoFactory;
        this.roomDtoFactory = roomDtoFactory;
    }

    @Transactional
    public void saveHotel(CreateHotel createHotel) {
        //TODO STB-1
        hotelRepository.save(Hotel.builder()
                    .stars(createHotel.getStars())
                    .name(createHotel.getName())
                    .address(Address.builder()
                            .city(createHotel.getCity())
                            .country(createHotel.getCountry())
                            .houseNumber(createHotel.getHouseNumber())
                            .street(createHotel.getStreet())
                            .postalCode(createHotel.getPostalCode())
                            .build())
                    .description(createHotel.getDescription())
            .build());
    }

    //TODO STB-3
    @Transactional
    public HotelDto getHotelByIdUser(Long id, Integer guests, String startDate, String endDate) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException(id));
        List<Room> freeRooms = roomAvailabilityService
                .getFreeRooms(hotel.getId(), startDate, endDate, null, guests, null);
        HotelDto hotelDto = hotelDtoFactory.createHotelDto(hotel);
        Set<String> uniqueRoomType = new HashSet<>();
        if (!hotel.getRoom().isEmpty()) {
            hotelDto.getRoomDto().addAll(freeRooms.stream()
                    .map(room -> {
                        return roomDtoFactory.createRoomDto(room, startDate, endDate);
                    })
                    .filter(dto -> uniqueRoomType.add(dto.getRoomType()))
                    .toList());
        }
        return hotelDto;
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id).orElseThrow(
                () -> new HotelNotFoundException(id)
        );
    }

    public void saveHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    @Transactional
    public void deleteHotelById(Long id) throws HotelNotFoundException{
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException(id));
        hotelRepository.delete(hotel);
    }

    @Transactional
    @Cacheable(value = "hotelSearch", key = "{#root.methodName, #stars, #country, #city, #page}")
    public Page<HotelDto> searchHotelByFilter(Integer stars, String country, String city, int page) {
        Specification<Hotel> spec = Specification.where(null);
        if (stars != null) spec = spec.and(HotelSpecification.hasStars(stars));
        if (country != null) spec = spec.and(HotelSpecification.hasCountry(country));
        if (city != null) spec = spec.and(HotelSpecification.hasCity(city));

        return hotelRepository.findAll(spec, CustomPageable.getPageable(page))
                .map(hotelDtoFactory::createHotelDto);
    }
}
