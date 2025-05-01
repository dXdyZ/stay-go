package com.lfey.statygo.service;

import com.lfey.statygo.dto.BookingRoom;
import com.lfey.statygo.entity.*;
import com.lfey.statygo.kafka.KafkaProducer;
import com.lfey.statygo.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    KafkaProducer kafkaProducer;

    @Mock
    RoomAvailabilityService roomAvailabilityService;

    @InjectMocks
    BookingService bookingService;

    private static Hotel hotel;
    private static Room room;
    private static Booking booking;

    @BeforeEach
    void init() {
        hotel = Hotel.builder()
                .id(1L)
                .name("test hotel")
                .stars(5)
                .grade(4.3)
                .address(Address.builder()
                        .id(1L)
                        .country("Russian")
                        .city("Moscow")
                        .street("moscow")
                        .houseNumber("23")
                        .postalCode(12311L)
                        .build())
                .description("Hello i`am new hotel for test method")
                .build();
        room = Room.builder()
                .id(1L)
                .number(123)
                .autoApprove(true)
                .hotel(hotel)
                .roomType(RoomType.LUX)
                .bedType(BedType.SINGLE)
                .capacity(1)
                .isActive(true)
                .pricePerDay(123.1)
                .description("This room very very lux for faking")
                .build();
        hotel.getRoom().add(room);
        booking = Booking.builder()
                .id(1L)
                .hotel(hotel)
                .room(room)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(2))
                .username("test")
                .totalPrice(246.2)
                .createDate(Instant.now())
                .build();

    }

    //Test don`t success
    @Test
    void bookingRoomValidData_AtRoomAutoApproveTrue() {
        var hotelId = 1L;
        var roomType = "LUX";
        var startDate = LocalDate.now().toString();
        var endDate = LocalDate.now().plusDays(2).toString();
        var guests = 1;
        var numberOfRoom = 1;
        var userName = "test";
        var bookingRoom = BookingRoom.builder()
                .hotelId(hotelId)
                .startDate(startDate)
                .endDate(endDate)
                .roomType(roomType)
                .guests(guests)
                .numberOfRooms(numberOfRoom)
                .build();

        doReturn(List.of(room)).when(this.roomAvailabilityService).getFreeRooms(
                hotelId, startDate, endDate, roomType, guests, numberOfRoom
        );
        var returnBooking = booking;
        returnBooking.setBookingStatus(BookingStatus.CONFIRMED);
        doReturn(List.of(returnBooking)).when(this.bookingRepository).saveAll(argThat(saveBooking -> {
            return saveBooking.iterator().hasNext();
        }));

        bookingService.bookingRoom(bookingRoom, userName);

        verify(this.roomAvailabilityService).getFreeRooms(
                hotelId, startDate, endDate, roomType, guests, numberOfRoom
        );

        verify(this.bookingRepository).saveAll(argThat(actBooking -> {
            Iterator<Booking> iter = actBooking.iterator();
            return iter.hasNext() && iter.next().getBookingStatus() == BookingStatus.CONFIRMED;
        }));
        verify(this.kafkaProducer).sendBookingDetails(argThat(bookingDetailsEvents -> {
            if (bookingDetailsEvents == null || bookingDetailsEvents.isEmpty()) return false;
            return bookingDetailsEvents.get(0).getUsername().equals("test");
        }));
    }
    @Test
    void bookingRoomValidData_AtRoomAutoApproveFalse() {
        var hotelId = 1L;
        var roomType = "LUX";
        var startDate = LocalDate.now().toString();
        var endDate = LocalDate.now().plusDays(2).toString();
        var guests = 1;
        var numberOfRoom = 1;
        var userName = "test";
        var bookingRoom = BookingRoom.builder()
                .hotelId(hotelId)
                .startDate(startDate)
                .endDate(endDate)
                .roomType(roomType)
                .guests(guests)
                .numberOfRooms(numberOfRoom)
                .build();
        room.setAutoApprove(false);

        doReturn(List.of(room)).when(this.roomAvailabilityService).getFreeRooms(
                hotelId, startDate, endDate, roomType, guests, numberOfRoom
        );
        var returnBooking = booking;
        returnBooking.setBookingStatus(BookingStatus.PENDING);
        doReturn(List.of(returnBooking)).when(this.bookingRepository).saveAll(argThat(saveBooking -> {
            return saveBooking.iterator().hasNext();
        }));

        bookingService.bookingRoom(bookingRoom, userName);

        verify(this.roomAvailabilityService).getFreeRooms(
                hotelId, startDate, endDate, roomType, guests, numberOfRoom
        );

        verify(this.bookingRepository).saveAll(argThat(actBooking -> {
            Iterator<Booking> iter = actBooking.iterator();
            return iter.hasNext() && iter.next().getBookingStatus() == BookingStatus.PENDING;
        }));
        verify(this.kafkaProducer).sendBookingDetails(argThat(bookingDetailsEvents -> {
            if (bookingDetailsEvents == null || bookingDetailsEvents.isEmpty()) return false;
            return bookingDetailsEvents.get(0).getUsername().equals("test");
        }));
    }
}








