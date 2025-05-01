package com.lfey.authservice.service;

import com.lfey.authservice.dto.kafka.RegistrationEvent;
import com.lfey.authservice.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationEventDispatcher {
    private final KafkaProducer kafkaProducer;

    @Autowired
    public RegistrationEventDispatcher(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    //TODO В дальнейшем можно добавить логирование
    public void dispatcherRegistrationEvent(RegistrationEvent registrationEvent) {
        kafkaProducer.sendRegistrationEvent(registrationEvent);
    }
}
