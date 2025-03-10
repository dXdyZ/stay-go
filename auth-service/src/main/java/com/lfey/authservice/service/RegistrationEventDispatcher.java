package com.lfey.authservice.service;

import com.lfey.authservice.dto.rabbit.RegistrationEvent;
import com.lfey.authservice.rabbit.RabbitSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationEventDispatcher {
    private final RabbitSender rabbitSender;

    @Autowired
    public RegistrationEventDispatcher(RabbitSender rabbitSender) {
        this.rabbitSender = rabbitSender;
    }

    //TODO В дальнейшем можно добавить логирование
    public void dispatcherRegistrationEvent(RegistrationEvent registrationEvent) {
        rabbitSender.sendRegistrationEvent(registrationEvent);
    }
}
