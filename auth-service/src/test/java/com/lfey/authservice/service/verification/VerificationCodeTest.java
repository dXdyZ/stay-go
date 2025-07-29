package com.lfey.authservice.service.verification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificationCodeTest {
    @Mock
    UserRegService userRegService;
    @Mock
    EmailUpdateService emailUpdateService;

    @InjectMocks
    VerificationCode verificationCode;

    @Test
    void verificationRegistration() {
        
    }
}