package com.lfey.authservice.service;

import com.lfey.authservice.dto.UserRegistrationDto;
import com.lfey.authservice.dto.kafka.EventType;
import com.lfey.authservice.entity.redis.UserRegistration;
import com.lfey.authservice.repository.jpaRepository.UserRepository;
import com.lfey.authservice.service.clients.UserClientService;
import com.lfey.authservice.service.security_service.TokenService;
import com.lfey.authservice.service.verification.GenerationCode;
import com.lfey.authservice.service.verification.VerificationCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    GenerationCode generationCode;
    @Mock
    TokenService tokenService;
    @Mock
    VerificationCode verificationCode;
    @Mock
    UserRepository userRepository;
    @Mock
    UserClientService userClientService;
    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    AuthService authService;

    @Test
    void registerUser_WhenDuplicateUserNotExists() {
        var username = "test";
        var encodePassword = "encodePassword";
        var userRegistrationDTO = new UserRegistrationDto(username, null, "password", "899003232");
        doReturn(false).when(this.userRepository).existsByUsername(username);
        doReturn(encodePassword).when(this.passwordEncoder).encode("password");

        this.authService.registerUser(userRegistrationDTO);

        verify(this.userRepository).existsByUsername(username);
        verify(this.passwordEncoder).encode("password");
        verify(this.generationCode).generateCode(UserRegistration.builder()
                .username(username)
                .email(userRegistrationDTO.email())
                .password(encodePassword)
                .phoneNumber("")
                .build(), EventType.REGISTRATION);
    }
}










