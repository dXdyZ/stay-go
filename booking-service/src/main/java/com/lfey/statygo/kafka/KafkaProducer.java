package com.lfey.statygo.kafka;

import com.lfey.statygo.dto.BookingDetailsEvent;
import com.lfey.statygo.dto.PendingBookingNotification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String,  Object> kafkaTemplate;

    @Value("${app.topics.hotel-booking-event.name}")
    private String hotelBookingEventTopicName;

    @Value("${app.topics.booking-notification.name}")
    private String hotelBookingNotificationTopicName;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBookingDetailsNotification(List<BookingDetailsEvent> bookingDetailsEvent) {
        kafkaTemplate.send(hotelBookingEventTopicName,
                bookingDetailsEvent.get(0).getUserPublicId() + "|" + bookingDetailsEvent.get(0).getBookingId(),
                bookingDetailsEvent);
    }

    public void sendPendingBookingNotification(List<PendingBookingNotification> pendingBookingNotifications) {
        kafkaTemplate.send(hotelBookingNotificationTopicName,
                pendingBookingNotifications.get(0).getUserPublicId() + "|" +
                        pendingBookingNotifications.get(0).getBookingId());
    }
}
