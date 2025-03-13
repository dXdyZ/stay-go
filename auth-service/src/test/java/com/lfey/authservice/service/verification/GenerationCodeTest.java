package com.lfey.authservice.service.verification;

import com.lfey.authservice.dto.UserReg;
import com.lfey.authservice.dto.rabbit.EventType;
import com.lfey.authservice.dto.rabbit.RegistrationEvent;
import com.lfey.authservice.service.RegistrationEventDispatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GenerationCodeTest {

    @Mock
    UserRegService userRegService;

    @Mock
    RegistrationEventDispatcher registrationEventDispatcher;

    @InjectMocks
    GenerationCode generationCode;

    @Test
    void generationCode_WhenUserNotExistsInDataBase() {
        //given
        var email = "test@test.com";
        var userReg = UserReg.builder()
                .email(email)
                .username("test")
                .phoneNumber("123455")
                .build();
        doReturn(false).when(this.userRegService).existsByEmail(email);

        //when
        this.generationCode.generateCode(userReg, EventType.REGISTRATION);

        //then
        verify(this.userRegService).saveUserData(argThat(actuealUserReg -> {
            return actuealUserReg.getUsername().equals(userReg.getUsername()) &&
                    actuealUserReg.getEmail().equals(userReg.getEmail()) &&
                    actuealUserReg.getCode() != null &&
                    actuealUserReg.getCode().length() == 6;
        }));
        verify(this.registrationEventDispatcher)
                .dispatcherRegistrationEvent(this.generationCode.getRegistrationEvent(userReg, EventType.REGISTRATION));
    }

    @Test
    void generationCode_WhenUserExistsInDataBase() {
        //given
        var email = "test@test.com";
        var userReg = UserReg.builder()
                .email(email)
                .username("test")
                .phoneNumber("123455")
                .build();
        doReturn(true).when(this.userRegService).existsByEmail(email);

        //when
        this.generationCode.generateCode(userReg, EventType.REGISTRATION);

        //then
        verify(this.userRegService).removeUserRegByEmail(email);
        verify(this.userRegService).saveUserData(argThat(actuealUserReg -> {
            return actuealUserReg.getUsername().equals(userReg.getUsername()) &&
                    actuealUserReg.getEmail().equals(userReg.getEmail()) &&
                    actuealUserReg.getCode() != null &&
                    actuealUserReg.getCode().length() == 6;
        }));
        verify(this.registrationEventDispatcher)
                .dispatcherRegistrationEvent(this.generationCode.getRegistrationEvent(userReg, EventType.REGISTRATION));
    }
}











