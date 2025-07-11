package com.lfey.statygo.component.factory;

import com.lfey.statygo.dto.HotelDto;
import com.lfey.statygo.entity.Hotel;
import org.springframework.stereotype.Component;

public class HotelFactory {
    public static HotelDto createHotelDto(Hotel hotel) {
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
                .photoDto(hotel.getPhotos().stream()
                        .map(PhotoDtoFactory::createPhotoDto)
                        .toList())
                .hotelType(hotel.getHotelType().name())
                .build();
    }

    public static HotelDto createHotelDtoSearch(Hotel hotel) {
       return HotelDto.builder()
                .hotelId(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .grade(hotel.getGrade())
                .stars(hotel.getStars())
                .address(String.format("%s, %s",
                        hotel.getAddress().getCountry(),
                        hotel.getAddress().getCity()))
                .hotelType(hotel.getHotelType().name())
                .build();
    }
}
