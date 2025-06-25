package com.lfey.statygo.service;

import com.lfey.statygo.component.CustomDateFormatter;
import com.lfey.statygo.component.CustomPageable;
import com.lfey.statygo.component.PageResponse;
import com.lfey.statygo.component.factory.BookingDetailsEventFactory;
import com.lfey.statygo.component.factory.BookingFactory;
import com.lfey.statygo.component.factory.BookingHistoryFactory;
import com.lfey.statygo.dto.BookingDetailsEvent;
import com.lfey.statygo.dto.BookingDto;
import com.lfey.statygo.dto.BookingHistoryDto;
import com.lfey.statygo.dto.BookingRoomDto;
import com.lfey.statygo.entity.Booking;
import com.lfey.statygo.entity.BookingStatus;
import com.lfey.statygo.exception.BookingNotFoundException;
import com.lfey.statygo.kafka.KafkaProducer;
import com.lfey.statygo.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final KafkaProducer kafkaProducer;
    private final RoomAvailabilityService roomAvailabilityService;

    public BookingService(BookingRepository bookingRepository, KafkaProducer kafkaProducer,
                          RoomAvailabilityService roomAvailabilityService) {
        this.bookingRepository = bookingRepository;
        this.kafkaProducer = kafkaProducer;
        this.roomAvailabilityService = roomAvailabilityService;
    }

    @Transactional
    public void bookingRoom(BookingRoomDto bookingRoomDto, String username) {
        CustomDateFormatter.dateVerification(bookingRoomDto.getStartDate(), bookingRoomDto.getEndDate());

        List<Booking> saveBooking = bookingRepository.saveAll(
                roomAvailabilityService.getFreeRooms(bookingRoomDto.getHotelId(), bookingRoomDto.getStartDate(),
                        bookingRoomDto.getEndDate(), bookingRoomDto.getRoomType(),
                                bookingRoomDto.getGuests(), bookingRoomDto.getNumberOfRooms()).stream()
                        .map(freeRoom -> {
                            Booking booking = BookingFactory.createBooking(freeRoom, username, bookingRoomDto);
                            if (freeRoom.getAutoApprove()) {
                                booking.setBookingStatus(BookingStatus.CONFIRMED);
                            } else {
                                booking.setBookingStatus(BookingStatus.PENDING);
                                //TODO Разобраться с созданием сообщения админу
//                                kafkaProducer.sendPendingBookingNotification();
                            }
                            return booking;
                        })
                        .toList());

        kafkaProducer.sendBookingDetailsNotification(getGroupedBookingDetailsByRoomType(saveBooking, username));
    }


    private List<BookingDetailsEvent> getGroupedBookingDetailsByRoomType(List<Booking> bookings, String username) {
        return bookings.stream()
                .map(booking -> {
                    return BookingDetailsEventFactory.createBookingDetailsEvent(booking, username);
                })
                .collect(Collectors.groupingBy(
                        BookingDetailsEvent::getRoomType,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    BookingDetailsEvent first = list.get(0);
                                    return BookingDetailsEventFactory.createBookingDetailsEventByGroup(first, list.size());
                                }
                        )
                ))
                .values()
                .stream()
                .toList();
    }

    public BookingDto getBookingById(Long id) {
        return BookingFactory.createBookingDto(bookingRepository.findById(id).orElseThrow(
                () -> new BookingNotFoundException(String.format("Booking by id: %s not found", id))
        ));
    }

    public PageResponse<BookingDto> getHotelReservationOverPeriod(Long hotelId, String startDate, String endDate) {
        CustomDateFormatter.dateVerification(startDate, endDate);
        return PageResponse.fromPage(bookingRepository.findAllBookingByPeriodAtHotel(hotelId,
                CustomDateFormatter.localDateFormatter(startDate),
                CustomDateFormatter.localDateFormatter(Objects.requireNonNullElse(endDate, LocalDate.now().toString())),
                CustomPageable.getPageable(0, 7)), BookingFactory::createBookingDto);
    }

    public PageResponse<BookingDto> getAllHotelReservation(Long hotelId) {
        return PageResponse.fromPage(bookingRepository.findAllByHotel_Id(hotelId,
                        CustomPageable.getPageable(0, 7)),
                BookingFactory::createBookingDto);
    }

    public PageResponse<BookingHistoryDto> getUserBookingHistory(String username, int page) {
        return PageResponse.fromPage(
                bookingRepository.findAllByUsername(username, CustomPageable.getPageable(page, 9)),
                BookingHistoryFactory::createBookingHistoryDto);
    }
}








