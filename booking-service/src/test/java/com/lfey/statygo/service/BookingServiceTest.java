package com.lfey.statygo.service;

import com.lfey.statygo.kafka.KafkaProducer;
import com.lfey.statygo.repository.BookingRepository;
import com.lfey.statygo.service.BookingService;
import com.lfey.statygo.service.RoomService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    RoomService roomService;

    @Mock
    KafkaProducer kafkaProducer;

    @InjectMocks
    BookingService bookingService;



}









