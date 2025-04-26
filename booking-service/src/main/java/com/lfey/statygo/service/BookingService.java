package com.lfey.statygo.service;

import com.lfey.statygo.component.CustomDateFormatter;
import com.lfey.statygo.component.PriceCalculate;
import com.lfey.statygo.component.factory.BookingDetailsEventFactory;
import com.lfey.statygo.component.factory.BookingFactory;
import com.lfey.statygo.dto.BookingDetailsEvent;
import com.lfey.statygo.dto.BookingRoom;
import com.lfey.statygo.entity.Booking;
import com.lfey.statygo.entity.BookingStatus;
import com.lfey.statygo.kafka.KafkaProducer;
import com.lfey.statygo.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final KafkaProducer kafkaProducer;
    private final RoomAvailabilityService roomAvailabilityService;
    private final BookingDetailsEventFactory bookingDetailsEventFactory;
    private final BookingFactory bookingFactory;

    public BookingService(BookingRepository bookingRepository, KafkaProducer kafkaProducer,
                          RoomAvailabilityService roomAvailabilityService, BookingDetailsEventFactory bookingDetailsEventFactory,
                          BookingFactory bookingFactory) {
        this.bookingRepository = bookingRepository;
        this.kafkaProducer = kafkaProducer;
        this.roomAvailabilityService = roomAvailabilityService;
        this.bookingDetailsEventFactory = bookingDetailsEventFactory;
        this.bookingFactory = bookingFactory;
    }

    @Transactional
    public void bookingRoom(BookingRoom bookingRoom, String username) {
        List<Booking> bookings = roomAvailabilityService.getFreeRooms(bookingRoom.getHotelId(), bookingRoom.getStartDate(),
                        bookingRoom.getEndDate(), bookingRoom.getRoomType(), bookingRoom.getGuests(), bookingRoom.getNumberOfRooms())
                .stream()
                .map(freeRoom -> {
                    Booking booking = bookingFactory.createBooking(freeRoom, username, bookingRoom);
                    if (freeRoom.getAutoApprove()) {
                        booking.setBookingStatus(BookingStatus.CONFIRMED);
                    } else {
                        booking.setBookingStatus(BookingStatus.PENDING);
                    }
                    return booking;
                })
                .toList();
        //TODO STB-5
        List<Booking> saveBooking = (List<Booking>) bookingRepository.saveAll(bookings);

        kafkaProducer.sendBookingDetails(getGroupedBookingDetailsByRoomType(saveBooking, username));
    }


    private List<BookingDetailsEvent> getGroupedBookingDetailsByRoomType(List<Booking> bookings, String username) {
        return bookings.stream()
                .map(booking -> {
                    return bookingDetailsEventFactory.createBookingDetailsEvent(booking, username);
                })
                .collect(Collectors.groupingBy(
                        BookingDetailsEvent::getRoomType,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    BookingDetailsEvent first = list.get(0);
                                    return bookingDetailsEventFactory.createBookingDetailsEventByGroup(first, list.size());
                                }
                        )
                ))
                .values()
                .stream()
                .toList();
    }
}








