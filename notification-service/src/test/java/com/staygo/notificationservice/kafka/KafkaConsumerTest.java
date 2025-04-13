package com.staygo.notificationservice.kafka;

import com.staygo.notificationservice.entity.EventType;
import com.staygo.notificationservice.entity.RegistrationEvent;
import com.staygo.notificationservice.service.EmailSendService;
import com.staygo.notificationservice.service.UserClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    @Mock
    EmailSendService emailSendService;

    @Mock
    UserClientService userClientService;

    @InjectMocks
    KafkaConsumer kafkaConsumer;

    @Test
    void receiveRegisterEvent() {
        var email = "test@test.com";
        var confirmCode = "123456";
        var regEvent = new RegistrationEvent(email, confirmCode, EventType.REGISTRATION);

        this.kafkaConsumer.receiveRegisterEvent(regEvent);

        verify(this.emailSendService).sendMail(email, "Registration confirmation code", confi);
    }
}






