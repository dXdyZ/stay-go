package com.lfey.statygo.service;

import com.lfey.statygo.dto.HotelDto;
import com.lfey.statygo.entity.*;
import com.lfey.statygo.exception.HotelNotFoundException;
import com.lfey.statygo.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    private static final Logger log = LoggerFactory.getLogger(HotelServiceTest.class);

    @Mock
    HotelRepository hotelRepository;

    @Mock
    RoomAvailabilityService roomAvailabilityService;

    @InjectMocks
    HotelService hotelService;

    private Hotel hotel;

    private List<Room> rooms;

    @BeforeEach
    void init() {
        hotel = Hotel.builder()
                .id(1L)
                .name("test hotel")
                .description("test new hotel")
                .grade(5.0)
                .stars(5)
                .address(Address.builder()
                        .id(1L)
                        .country("Russian")
                        .country("Moscow")
                        .street("Lenina")
                        .houseNumber("123")
                        .postalCode(12345L)
                        .build())
                .build();
        rooms = new ArrayList<>() {
            {
                add(Room.builder().id(1L).hotel(hotel).roomType(RoomType.LUX)
                        .number(123).pricePerDay(123.0).autoApprove(false)
                        .bedType(BedType.DOUBLE).capacity(2).description("test rooms")
                        .isActive(true).build());
                add(Room.builder().id(3L).hotel(hotel)
                        .roomType(RoomType.LUX).number(123).pricePerDay(123.0)
                        .autoApprove(false).bedType(BedType.DOUBLE).capacity(2)
                        .description("test rooms").isActive(true).build());
                add(Room.builder().id(2L).hotel(hotel)
                        .roomType(RoomType.BUSINESS).number(123).pricePerDay(123.0)
                        .autoApprove(false).bedType(BedType.DOUBLE).capacity(2)
                        .description("test rooms").isActive(true).build());
            }
        };
        hotel.getRoom().addAll(rooms);
    }

    @Test
    void getHotelById_WhenHotelExists() {
        var hotel = Hotel.builder()
                .id(1L)
                .build();
        doReturn(Optional.of(hotel)).when(this.hotelRepository).findById(1L);

        Hotel response = this.hotelService.getHotelById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void getHotelById_WhenHotelNotExists_ThrowHotelNotFoundException() {
        doReturn(Optional.empty()).when(this.hotelRepository).findById(1L);
        HotelNotFoundException exception = assertThrows(
                HotelNotFoundException.class,
                () -> hotelService.getHotelById(1L)
        );
        assertEquals("Hotel by: 1 not found", exception.getMessage());
    }

    @Test
    void getHotelByIdUser_WhenHotelExistsWithRoomsAndValidDate() {
        var id = 1L;
        var startDate = "2024-04-01";
        var endDate = "2024-04-05";

        doReturn(Optional.of(hotel)).when(this.hotelRepository).findById(id);

        HotelDto response = this.hotelService.getHotelByIdUser(id, 2, startDate, endDate);

        assertNotNull(response);
        assertEquals(2, response.getRoomDto().size());
        assertEquals(492.0, response.getRoomDto().get(0).getTotalPrice());

    }
}










