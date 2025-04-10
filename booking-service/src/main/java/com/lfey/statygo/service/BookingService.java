package com.lfey.statygo.service;

import com.lfey.statygo.component.CustomDateFormatter;
import com.lfey.statygo.component.PriceCalculate;
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
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final KafkaProducer kafkaProducer;
    private final RoomAvailabilityService roomAvailabilityService;

    public BookingService(BookingRepository bookingRepository, KafkaProducer kafkaProducer, RoomAvailabilityService roomAvailabilityService) {
        this.bookingRepository = bookingRepository;
        this.kafkaProducer = kafkaProducer;
        this.roomAvailabilityService = roomAvailabilityService;
    }

    @Transactional
    public void bookingRoom(BookingRoom bookingRoom, String username) {
        List<Booking> bookings = roomAvailabilityService.getFreeRooms(bookingRoom.getHotelId(), bookingRoom.getStartDate(),
                        bookingRoom.getEndDate(), bookingRoom.getRoomType(), bookingRoom.getGuests(), bookingRoom.getNumberOfRooms())
                .stream()
                .map(freeRoom -> {
                    Booking booking = Booking.builder()
                            .room(freeRoom)
                            .hotel(freeRoom.getHotel())
                            .createDate(Instant.now())
                            .startDate(CustomDateFormatter.localDateFormatter(bookingRoom.getStartDate()))
                            .endDate(CustomDateFormatter.localDateFormatter(bookingRoom.getEndDate()))
                            .username(username)
                            .totalPrice(PriceCalculate.calculationTotalPrice(freeRoom.getPricePerDay(),
                                    CustomDateFormatter.localDateFormatter(bookingRoom.getStartDate()),
                                    CustomDateFormatter.localDateFormatter(bookingRoom.getEndDate())))
                            .build();

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


    public List<BookingDetailsEvent> getGroupedBookingDetailsByRoomType(List<Booking> bookings, String username) {
        return bookings.stream()
                .map(booking -> {
                    return BookingDetailsEvent.builder()
                            .bookingId(booking.getId())
                            .hotelName(booking.getHotel().getName())
                            .hotelAddress(booking.getHotel().getAddress().getHumanReadableAddress())
                            .startDate(booking.getStartDate().toString())
                            .endDate(booking.getEndDate().toString())
                            .roomType(booking.getRoom().getRoomType().name())
                            .username(username)
                            .totalPrice(booking.getTotalPrice())
                            .bookingStatus(booking.getBookingStatus().name())
                            .reservedRooms(1)
                            .build();
                })
                .collect(Collectors.groupingBy(
                        BookingDetailsEvent::getRoomType,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                
                                    BookingDetailsEvent first = list.get(0);
                                    return BookingDetailsEvent.builder()
                                            .hotelName(first.getHotelName())
                                            .hotelAddress(first.getHotelAddress())
                                            .roomType(first.getRoomType())
                                            .startDate(first.getStartDate())
                                            .endDate(first.getEndDate())
                                            .username(first.getUsername())
                                            .totalPrice(first.getTotalPrice() * list.size())
                                            .bookingStatus(first.getBookingStatus())
                                            .reservedRooms(list.size()) 
                                            .build();
                                }
                        )
                ))
                .values()
                .stream()
                .toList();
    }
}








