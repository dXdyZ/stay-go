package com.lfey.statygo.service;

import com.lfey.statygo.component.CustomDateFormatter;
import com.lfey.statygo.dto.BookingRoom;
import com.lfey.statygo.entity.*;
import com.lfey.statygo.exception.NoRoomsAvailableException;
import com.lfey.statygo.service.RoomAvailabilityService;
import com.lfey.statygo.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class RoomAvailabilityServiceTest {

    @Mock
    RoomService roomService;

    @InjectMocks
    RoomAvailabilityService roomAvailabilityService;

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
    void getFreeRoomsForBooking_WhenExistRoomOnTheDates() {
        var hotelId = 1L;
        var startDate = "2025-04-12";
        var endDate = "2025-04-15";
        var roomType = "LUX";
        var guests = 2;
        var numberOfRooms = 1;

        doReturn(List.of(rooms.get(0), rooms.get(1))).when(this.roomService).getAvailableRoom(
                hotelId, guests, CustomDateFormatter.localDateFormatter(startDate),
                CustomDateFormatter.localDateFormatter(endDate), RoomType.valueOf(roomType)
        );

        List<Room> result = this.roomAvailabilityService.getFreeRooms(hotelId, startDate, endDate, roomType, guests, numberOfRooms);

        assertThat(result)
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void getFreeRoomsForBooking_WhenNotExistRoomOnTheDates_ThrowNoRoomsAvailableException() {
        var hotelId = 1L;
        var startDate = "2025-04-12";
        var endDate = "2025-04-15";
        var roomType = "LUX";
        var guests = 2;
        var numberOfRooms = 1;

        doReturn(List.of()).when(this.roomService).getAvailableRoom(
                hotelId, guests, CustomDateFormatter.localDateFormatter(startDate),
                CustomDateFormatter.localDateFormatter(endDate), RoomType.valueOf(roomType)
        );

        assertThatThrownBy(() -> this.roomAvailabilityService.getFreeRooms(hotelId, startDate, endDate, roomType, guests, numberOfRooms))
                .isInstanceOf(NoRoomsAvailableException.class)
                .hasMessage("No available rooms of this type on the specified date");
    }

    @Test
    void getFreeRoomsForBooking_WhenExistRoomOnTheDatesForSearchingById() {
        var hotelId = 1L;
        var startDate = "2025-04-12";
        var endDate = "2025-04-15";
        var roomType = "LUX";
        var guests = 2;

        doReturn(List.of(rooms.get(0), rooms.get(1))).when(this.roomService).getAvailableRoom(
                hotelId, guests, CustomDateFormatter.localDateFormatter(startDate),
                CustomDateFormatter.localDateFormatter(endDate), RoomType.valueOf(roomType)
        );
    }
}







