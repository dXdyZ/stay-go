package com.staygo.notificationservice.kafka;

import com.staygo.notificationservice.entity.BookingDetailsEvent;
import com.staygo.notificationservice.entity.RegistrationEvent;
import com.staygo.notificationservice.service.EmailSendService;
import com.staygo.notificationservice.service.UserClientService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KafkaConsumer {
    private final EmailSendService emailSendService;
    private final UserClientService userClientService;

    public KafkaConsumer(EmailSendService emailSendService, UserClientService userClientService) {
        this.emailSendService = emailSendService;
        this.userClientService = userClientService;
    }

    @KafkaListener(topics = "${app.topics.email-verification}")
    public void receiveRegisterEvent(@Payload RegistrationEvent registrationEvent) {
        switch (registrationEvent.getEventType()) {
            case REGISTRATION -> {
                emailSendService.sendMail(registrationEvent.getEmail(),
                        "Registration confirmation code", registrationEvent.getConfirmCode());
            }
            case EMAIL_RESET -> {
                emailSendService.sendMail(registrationEvent.getEmail(),
                        "Email change confirmation code", registrationEvent.getConfirmCode());
            }
            case PASSWORD_RESET -> {
                emailSendService.sendMail(registrationEvent.getEmail(),
                        "Password change confirmation code", registrationEvent.getConfirmCode());
            }
        }
    }

    @KafkaListener(topics = "${app.topics.hotel-booking-event}")
    public void receiveBookingEvent(@Payload List<BookingDetailsEvent> bookingDetailsEvents) {
        emailSendService.sendMail(userClientService.getUserByUsername(
                bookingDetailsEvents.get(0).getUsername()).getEmail(),
                "Booking details",
                bookingDetailsEvents.stream()
                        .map(BookingDetailsEvent::toPrettyString)
                        .collect(Collectors.joining("\n")));
    }
}








