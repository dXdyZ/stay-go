package com.lfey.authservice.service.verification;

import com.lfey.authservice.dto.kafka.EventType;
import com.lfey.authservice.entity.redis.EmailUpdate;
import com.lfey.authservice.entity.redis.UserRegistration;
import com.lfey.authservice.service.RegistrationEventDispatcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerationCodeTest {
    @Mock
    UserRegService userRegService;
    @Mock
    EmailUpdateService emailUpdateService;
    @Mock
    RegistrationEventDispatcher registrationEventDispatcher;

    @InjectMocks
    GenerationCode generationCode;

    private UUID publicId;
    private UserRegistration userRegistration;
    private EmailUpdate emailUpdate;


    @BeforeEach
    void init() {
        publicId = UUID.randomUUID();
        userRegistration = UserRegistration.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .phoneNumber("890998989")
                .build();
        emailUpdate = EmailUpdate.builder()
                .newEmail("email@email.com")
                .publicId(publicId)
                .build();
    }

    @Test
    void generateCode_Registration_WhenUserDoesntExistInTheCache() {
        var eventType = EventType.REGISTRATION;

        when(userRegService.existsByEmail(userRegistration.getEmail())).thenReturn(false);

        this.generationCode.generateCode(userRegistration, eventType);

        verify(this.userRegService).saveUserData(argThat(
                actRegUser -> actRegUser.getCode() != null && actRegUser.getCode().length() == 6));

        verify(this.registrationEventDispatcher).dispatcherRegistrationEvent(argThat(actRegEvent ->
                actRegEvent.getEmail().equals(userRegistration.getEmail()) && actRegEvent.getEventType().equals(eventType)));
        verify(this.userRegService, never()).removeUserRegByEmail(userRegistration.getEmail());
    }

    @Test
    void generateCode_Registration_WhenUserExistInTheCache() {
        var eventType = EventType.REGISTRATION;

        when(userRegService.existsByEmail(userRegistration.getEmail())).thenReturn(true);

        this.generationCode.generateCode(userRegistration, eventType);

        verify(this.userRegService).saveUserData(argThat(
                actRegUser -> actRegUser.getCode() != null && actRegUser.getCode().length() == 6));
        verify(this.userRegService).removeUserRegByEmail(userRegistration.getEmail());
        verify(this.registrationEventDispatcher).dispatcherRegistrationEvent(argThat(actRegEvent ->
                actRegEvent.getEmail().equals(userRegistration.getEmail()) && actRegEvent.getEventType().equals(eventType)));
    }


    @Test
    void generateCode_UpdateEmail_WhenUserDoesntExistsInTheCache() {

        when(this.emailUpdateService.existsByEmail(emailUpdate.getNewEmail())).thenReturn(false);

        this.generationCode.generateCode(emailUpdate);

        verify(this.emailUpdateService).existsByEmail(emailUpdate.getNewEmail());
        verify(this.emailUpdateService).saveEmailUpdateData(argThat(
                actEmailUpdate -> actEmailUpdate.getCode() != null && actEmailUpdate.getCode().length() == 6));
        verify(this.registrationEventDispatcher).dispatcherRegistrationEvent(argThat(
                actEmailUpdateEv -> actEmailUpdateEv.getEmail().equals(emailUpdate.getNewEmail()) &&
                        actEmailUpdateEv.getEventType().equals(EventType.EMAIL_RESET) &&
                        actEmailUpdateEv.getConfirmCode() != null && actEmailUpdateEv.getConfirmCode().length() == 6));
        verify(this.emailUpdateService, never()).removeEmailUpdateDataByEmail(emailUpdate.getNewEmail());
    }

    @Test
    void generateCode_UpdateEmail_WhenUserExistsInTheCache() {
        when(this.emailUpdateService.existsByEmail(emailUpdate.getNewEmail())).thenReturn(true);

        this.generationCode.generateCode(emailUpdate);

        verify(this.emailUpdateService).existsByEmail(emailUpdate.getNewEmail());
        verify(this.emailUpdateService).saveEmailUpdateData(argThat(
                actEmailUpdate -> actEmailUpdate.getCode() != null && actEmailUpdate.getCode().length() == 6));
        verify(this.registrationEventDispatcher).dispatcherRegistrationEvent(argThat(
                actEmailUpdateEv -> actEmailUpdateEv.getEmail().equals(emailUpdate.getNewEmail()) &&
                        actEmailUpdateEv.getEventType().equals(EventType.EMAIL_RESET) &&
                        actEmailUpdateEv.getConfirmCode() != null && actEmailUpdateEv.getConfirmCode().length() == 6));
        verify(this.emailUpdateService).removeEmailUpdateDataByEmail(emailUpdate.getNewEmail());
    }
}









