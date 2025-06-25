package com.lfey.statygo;

import com.lfey.statygo.entity.*;
import com.lfey.statygo.repository.AddressRepository;
import com.lfey.statygo.repository.HotelRepository;
import com.lfey.statygo.repository.PhotoRepository;
import com.lfey.statygo.repository.RoomRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

//@Component
//public class TestDataLoader {
//
//    @Autowired
//    private HotelRepository hotelRepository;
//
//    @Autowired
//    private RoomRepository roomRepository;
//
//    @Autowired
//    private PhotoRepository photoRepository;
//
//    @Autowired
//    private AddressRepository addressRepository;
//
//    @PostConstruct
//    public void init() {
//        IntStream.rangeClosed(50, 500).forEach(i -> {
//            // Создаем адрес без явного сохранения
//            Address address = Address.builder()
//                    .country("Russian")
//                    .city("Moscow")
//                    .street("Tverskaya St")
//                    .houseNumber(String.valueOf(i))
//                    .postalCode(123456L)
//                    .build();
//
//            // Создаем отель и привязываем к нему адрес
//            Hotel hotel = Hotel.builder()
//                    .name("Hotel " + i)
//                    .stars(5)
//                    .grade(4.5 + i % 2 * 0.1)
//                    .description("Luxury hotel in Moscow, №" + i)
//                    .hotelType(HotelType.HOTEL)
//                    .build();
//            hotel.setAddress(address); // каскадное сохранение включено в классе Hotel
//            hotel = hotelRepository.save(hotel);
//
//            // Добавляем главное фото
//            Photo mainPhoto = Photo.builder()
//                    .fileName("main_photo_hotel_" + i + ".jpg")
//                    .fileSize(102400L)
//                    .mimeType("image/jpeg")
//                    .isMain(true)
//                    .build();
//            mainPhoto.setHotel(hotel);
//            photoRepository.save(mainPhoto);
//
//            // Добавляем комнаты
//            Hotel finalHotel = hotel;
//            IntStream.rangeClosed(1, 5).forEach(j -> {
//                Room room = Room.builder()
//                        .number(j * 100 + i)
//                        .capacity(1 + j % 2)
//                        .pricePerDay((double) (5000 + j * 1000 + i * 100))
//                        .description("Room type: " + RoomType.values()[j % RoomType.values().length])
//                        .bedType(BedType.values()[j % BedType.values().length])
//                        .roomType(RoomType.STANDARD)
//                        .autoApprove(j % 2 == 0)
//                        .build();
//                room.setHotel(finalHotel);
//                roomRepository.save(room);
//            });
//        });
//    }
//}