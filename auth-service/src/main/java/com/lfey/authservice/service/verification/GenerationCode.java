package com.lfey.authservice.service.verification;

import com.lfey.authservice.entity.UserRegistration;
import com.lfey.authservice.dto.kafka.EventType;
import com.lfey.authservice.dto.kafka.RegistrationEvent;
import com.lfey.authservice.service.RegistrationEventDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class GenerationCode {
    private final UserRegService userRegService;
    private final RegistrationEventDispatcher registrationEventDispatcher;

    @Autowired
    public GenerationCode(UserRegService userRegService,
                          RegistrationEventDispatcher registrationEventDispatcher) {
        this.userRegService = userRegService;
        this.registrationEventDispatcher = registrationEventDispatcher;
    }

    public void generateCode(UserRegistration userRegistration, EventType eventType) {
        userRegistration.setCode(String.format("%06d", new Random().nextInt(999999)));
        log.info("code set email: {}", userRegistration.getCode());
        if (!userRegService.existsByEmail(userRegistration.getEmail())) {
            userRegService.saveUserData(userRegistration);
            registrationEventDispatcher.dispatcherRegistrationEvent(getRegistrationEvent(userRegistration, eventType));
        } else {
            userRegService.removeUserRegByEmail(userRegistration.getEmail());
            userRegService.saveUserData(userRegistration);
            registrationEventDispatcher.dispatcherRegistrationEvent(getRegistrationEvent(userRegistration, eventType));
        }
    }

    private RegistrationEvent getRegistrationEvent(UserRegistration userRegistration, EventType eventType) {
        return RegistrationEvent.builder()
                .email(userRegistration.getEmail())
                .confirmCode(userRegistration.getCode())
                .eventType(eventType)
                .build();
    }
}
