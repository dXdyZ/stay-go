package com.staygo.notificationservice.rabbit;

import com.staygo.notificationservice.entity.RegistrationEvent;
import com.staygo.notificationservice.service.EmailSendService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class RabbitReceiver {
    private final EmailSendService emailSendService;

    @Autowired
    public RabbitReceiver(EmailSendService emailSendService) {
        this.emailSendService = emailSendService;
    }

    @RabbitListener(queues = "#{@queueRegistrationEvent}")
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
}
