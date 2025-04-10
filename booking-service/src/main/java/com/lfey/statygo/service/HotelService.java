package com.lfey.statygo.service;

import com.lfey.statygo.component.CustomDateFormatter;
import com.lfey.statygo.component.CustomPageable;
import com.lfey.statygo.component.PriceCalculate;
import com.lfey.statygo.dto.BookingRoom;
import com.lfey.statygo.dto.CreateHotel;
import com.lfey.statygo.dto.HotelDto;
import com.lfey.statygo.dto.RoomDto;
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


    @Autowired
    public HotelService(HotelRepository hotelRepository, RoomAvailabilityService roomAvailabilityService) {
        this.hotelRepository = hotelRepository;
        this.roomAvailabilityService = roomAvailabilityService;
    }

    @Transactional
    public Hotel saveHotel(CreateHotel createHotel) {
        //TODO STB-1
        return hotelRepository.save(Hotel.builder()
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
        HotelDto hotelDto = HotelDto.builder()
                .hotelId(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .grade(hotel.getGrade())
                .stars(hotel.getStars())
                .address(String.format("%s, %s, %s, %s, %s",
                        hotel.getAddress().getPostalCode(),
                        hotel.getAddress().getCountry(),
                        hotel.getAddress().getCity(),
                        hotel.getAddress().getStreet(),
                        hotel.getAddress().getHouseNumber()))
                .build();
        Set<String> uniqueRoomType = new HashSet<>();
        if (!hotel.getRoom().isEmpty()) {
            hotelDto.getRoomDto().addAll(freeRooms.stream()
                    .map(room -> {
                        return RoomDto.builder()
                                .roomType(room.getRoomType().name())
                                .capacity(room.getCapacity())
                                .totalPrice(PriceCalculate.calculationTotalPrice(
                                        room.getPricePerDay(),
                                        CustomDateFormatter.localDateFormatter(startDate),
                                        CustomDateFormatter.localDateFormatter(endDate)
                                ))
                                .bedType(room.getBedType().name())
                                .build();
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
    public List<Hotel> getByHotelByName(String name, int page) {
        return hotelRepository.findByName(name, CustomPageable.getPageable(page));
    }

    @Transactional
    public void deleteHotelById(Long id) throws HotelNotFoundException{
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException(id));
        hotelRepository.delete(hotel);
    }

    @Transactional
    @Cacheable(value = "hotelSearch", key = "{#root.methodName, #stars, #country, #city, #street, #houseNumber}")
    public Page<Hotel> searchHotelByFilter(Integer stars, String country, String city,
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
