package com.lfey.statygo;


import com.lfey.statygo.entity.*;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;

public class GenerationData {

    public static List<Hotel> generateHotel(int numberOfHotel, String country, String city, Double grade, Integer stars,
                                            HotelType hotelType, BedType bedType, Integer capacity, RoomType roomType) {
        List<Hotel> hotels = new ArrayList<>();
        Faker faker = new Faker();
        
        for (int i = 0; i < numberOfHotel; i++) {
            Hotel hotel = Hotel.builder()
                            .id((long) i)
                            .name(faker.company().name())
                            .description(faker.text().text(20))
                            .grade(grade == null ? faker.number().randomDouble(1, 0, 5) : grade)
                            .stars(stars == null ? faker.number().numberBetween(1, 5) : stars)
                            .address(Address.builder()
                                    .id((long) i)
                                    .country(country == null ? faker.address().country() : country)
                                    .city(city == null ? faker.address().city() : city)
                                    .street(faker.address().streetName())
                                    .houseNumber(faker.bothify("## ?"))
                                    .postalCode(Long.valueOf(faker.address().zipCode()))
                                    .build())
                            .hotelType(hotelType == null ? faker.options().option(HotelType.values()) : hotelType)
                    .build();

            for (int j = 0; j < 10; j++) {
                Room room = Room.builder()
                        .id(i + hotel.getId())
                        .hotel(hotel)
                        .roomType(roomType == null ? faker.options().option(RoomType.values()) : roomType)
                        .number(i)
                        .pricePerDay(Double.valueOf(faker.numerify("#####")))
                        .autoApprove(false)
                        .bedType(bedType == null ? faker.options().option(BedType.values()) : bedType)
                        .capacity(capacity == null ? faker.number().numberBetween(1, 4) : capacity)
                        .description(faker.text().text(20))
                        .isActive(true)
                .build();
                hotel.getRoom().add(room);
            }
            hotels.add(hotel);
        }
        return hotels;
    }
}
