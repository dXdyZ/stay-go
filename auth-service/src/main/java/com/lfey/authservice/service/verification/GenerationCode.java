package com.lfey.authservice.service.verification;

import com.lfey.authservice.dto.UserReg;
import com.lfey.authservice.dto.rabbit.EventType;
import com.lfey.authservice.dto.rabbit.RegistrationEvent;
import com.lfey.authservice.service.RegistrationEventDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

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

    public void generateCode(UserReg userReg, EventType eventType) {
        userReg.setCode(String.format("%06d", new Random().nextInt(999999)));
        if (!userRegService.existsByEmail(userReg.getEmail())) {
            userRegService.saveUserData(userReg);
            registrationEventDispatcher.dispatcherRegistrationEvent(getRegistrationEvent(userReg, eventType));
        } else {
            userRegService.removeUserRegByEmail(userReg.getEmail());
            userRegService.saveUserData(userReg);
            registrationEventDispatcher.dispatcherRegistrationEvent(getRegistrationEvent(userReg, eventType));
        }
    }

    public RegistrationEvent getRegistrationEvent(UserReg userReg, EventType eventType) {
        return RegistrationEvent.builder()
                .email(userReg.getEmail())
                .confirmCode(userReg.getCode())
                .eventType(eventType)
                .build();
    }
}
