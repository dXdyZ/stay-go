package com.lfey.statygo.kafka;

import com.lfey.statygo.dto.BookingDetailsEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, List<BookingDetailsEvent>> kafkaTemplate;

    @Value("${app.topics.hotel-booking-event.name}")
    private String hotelBookingEventTopicName;

    public KafkaProducer(KafkaTemplate<String, List<BookingDetailsEvent>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    
    public void sendBookingDetails(List<BookingDetailsEvent> bookingDetailsEvent) {
        kafkaTemplate.send(hotelBookingEventTopicName,
                bookingDetailsEvent.get(0).getUsername() + "|" + bookingDetailsEvent.get(0).getBookingId(),
                bookingDetailsEvent);
    }
}





