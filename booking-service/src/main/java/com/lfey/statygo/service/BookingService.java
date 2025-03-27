package com.lfey.statygo.service;

import com.lfey.statygo.component.CustomDateFormatter;
import com.lfey.statygo.dto.BookingRoom;
import com.lfey.statygo.entity.Booking;
import com.lfey.statygo.entity.BookingStatus;
import com.lfey.statygo.entity.Room;
import com.lfey.statygo.entity.RoomType;
import com.lfey.statygo.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomService roomService;

    public BookingService(BookingRepository bookingRepository, RoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
    }

    @Transactional
    public void bookingRoom(BookingRoom bookingRoom, String username) {
        List<Booking> bookings = gettingFreeRooms(bookingRoom).stream()
                .map(freeRoom -> {
                    Booking booking =  Booking.builder()
                            .room(freeRoom)
                            .hotel(freeRoom.getHotel())
                            .createDate(Instant.now())
                            .startDate(CustomDateFormatter.localDateFormatter(bookingRoom.getStartDate()))
                            .endDate(CustomDateFormatter.localDateFormatter(bookingRoom.getEndDate()))
                            .username(username)
                            .totalPrice(calculationTotalPrice(freeRoom.getPricePerDay(),
                                    CustomDateFormatter.localDateFormatter(bookingRoom.getStartDate()),
                                    CustomDateFormatter.localDateFormatter(bookingRoom.getEndDate())))
                            .build();

                    if (freeRoom.getAutoApprove()) {
                        booking.setBookingStatus(BookingStatus.CONFIRMED);
                    } else {
                        //TODO Сделать логику отправки сообщения администратору отеля для подтверждения
                        booking.setBookingStatus(BookingStatus.PENDING);
                    }
                    return booking;
                })
                .toList();
        //TODO STB-5
        bookingRepository.saveAll(bookings);
    }

    @Transactional
    public List<Room> gettingFreeRooms(BookingRoom bookingRoom) {
        List<Long> bookedRoomIds = bookingRepository.findReservationRange(bookingRoom.getHotelId(), bookingRoom.getGuests(),
                RoomType.valueOf(bookingRoom.getRoomType()), CustomDateFormatter.localDateFormatter(bookingRoom.getStartDate()),
                CustomDateFormatter.localDateFormatter(bookingRoom.getEndDate())).stream()
                .map(booking -> booking.getRoom().getId())
                .toList();
        List<Room> allRoomsId = roomService.getRoomByHotelIdAndCapacityAndRoomType(bookingRoom.getHotelId(), bookingRoom.getGuests(),
                RoomType.valueOf(bookingRoom.getRoomType()));
        return allRoomsId.stream()
                .filter(room -> !bookedRoomIds.contains(room.getId()))
                .limit(bookingRoom.getNumberOfRooms())
                .toList();
    }

    public Double calculationTotalPrice(Double price, LocalDate startDate, LocalDate endDate) {
        return CustomDateFormatter.getNumberOfDays(startDate, endDate) * price;
    }
}








