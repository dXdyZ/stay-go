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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
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

    //TODO STB-3
    @Transactional
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
