package com.lfey.authservice.service.verification;

import com.lfey.authservice.entity.redis.EmailUpdate;
import com.lfey.authservice.entity.redis.UserRegistration;
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
    private final EmailUpdateService emailUpdateService;
    private final RegistrationEventDispatcher registrationEventDispatcher;

    @Autowired
    public GenerationCode(UserRegService userRegService, EmailUpdateService emailUpdateService,
                          RegistrationEventDispatcher registrationEventDispatcher) {
        this.userRegService = userRegService;
        this.emailUpdateService = emailUpdateService;
        this.registrationEventDispatcher = registrationEventDispatcher;
    }

    public void generateCode(UserRegistration userRegistration, EventType eventType) {
        userRegistration.setCode(String.format("%06d", new Random().nextInt(999999)));
        if (userRegService.existsByEmail(userRegistration.getEmail())) {
            userRegService.removeUserRegByEmail(userRegistration.getEmail());
        }
        userRegService.saveUserData(userRegistration);
        registrationEventDispatcher.dispatcherRegistrationEvent(getRegistrationEvent(userRegistration, eventType));
    }

    public void generateCode(EmailUpdate emailUpdate) {
        emailUpdate.setCode(String.format("%06d", new Random().nextInt(999999)));
        if (emailUpdateService.existsByEmail(emailUpdate.getNewEmail())) {
            emailUpdateService.removeEmailUpdateDataByEmail(emailUpdate.getNewEmail());
        }
        emailUpdateService.saveEmailUpdateData(emailUpdate);
        registrationEventDispatcher.dispatcherRegistrationEvent(getRegistrationEvent(emailUpdate));
    }

    private RegistrationEvent getRegistrationEvent(UserRegistration userRegistration, EventType eventType) {
        return RegistrationEvent.builder()
                .email(userRegistration.getEmail())
                .confirmCode(userRegistration.getCode())
                .eventType(eventType)
                .build();
    }

    private RegistrationEvent getRegistrationEvent(EmailUpdate emailUpdate) {
        return RegistrationEvent.builder()
                .email(emailUpdate.getNewEmail())
                .confirmCode(emailUpdate.getCode())
                .eventType(EventType.EMAIL_RESET)
                .build();
    }
}
