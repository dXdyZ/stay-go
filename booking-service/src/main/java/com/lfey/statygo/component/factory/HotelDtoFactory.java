package com.lfey.statygo.component.factory;

import com.lfey.statygo.dto.HotelDto;
import com.lfey.statygo.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelDtoFactory {
    public HotelDto createHotelDto(Hotel hotel) {
        return HotelDto.builder()
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
    }
}
