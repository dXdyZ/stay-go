package com.lfey.authservice.service;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.dto.kafka.EventType;
import com.lfey.authservice.entity.jpa.RoleName;
import com.lfey.authservice.entity.jpa.Users;
import com.lfey.authservice.entity.redis.UserRegistration;
import com.lfey.authservice.exception.AuthenticationFailedException;
import com.lfey.authservice.exception.DuplicateUserException;
import com.lfey.authservice.exception.UserCacheDataNotFoundException;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        when(this.userRepository.existsByUsername(username)).thenReturn(false);
        when(this.passwordEncoder.encode("password")).thenReturn(encodePassword);

        this.authService.registerUser(userRegistrationDTO);

        verify(this.userRepository).existsByUsername(username);
        verify(this.passwordEncoder).encode("password");
        verify(this.generationCode).generateCode(UserRegistration.builder()
                .username(username)
                .email(userRegistrationDTO.email())
                .password(encodePassword)
                .phoneNumber("899003232")
                .build(), EventType.REGISTRATION);
    }

    @Test
    void registerUser_WhenDuplicateUserByUsername_ThrowDuplicateUserException() {
        var username = "test";
        var email = "test@email.com";
        var userRegistrationDTO = new UserRegistrationDto(username, email, "password", "899003232");

        when(this.userRepository.existsByUsername(username)).thenReturn(true);

        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> this.authService.registerUser(userRegistrationDTO )
        );

        assertEquals(exception.getMessage(), String.format("The user named: %s already exists",
                userRegistrationDTO.username()));
        verifyNoInteractions(this.passwordEncoder, this.generationCode);
    }

    @Test
    void registerUser_WhenDuplicateUserByEmail_ThrowDuplicateUserException() {
        var username = "test";
        var email = "test@email.com";
        var userRegistrationDTO = new UserRegistrationDto(username, email, "password", "899003232");

        when(this.userRepository.existsByUsername(username)).thenReturn(false);
        when(this.userClientService.getUserByEmailFromUserService(email)).thenReturn(new UserDetailsDto());

        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> this.authService.registerUser(userRegistrationDTO )
        );

        assertEquals(exception.getMessage(), String.format("The user email: %s already exists",
                userRegistrationDTO.email()));
        verifyNoInteractions(this.passwordEncoder, this.generationCode);
    }

    @Test
    void saveUserAfterRegistration_WhenCacheUserDataExists_ThenDoesntThrowException() {
        var email = "test@test.com";
        var code = "123456";
        var password = "password";
        var phoneNumber = "8906232323";
        var username = "username";
        var validationCode = new ValidationCodeDto(email, code);

        when(this.verificationCode.verificationRegistration(validationCode)).thenReturn(
                new UserRegistration(email, username, password, phoneNumber, code));

        Users result = this.authService.saveUserAfterRegistration(validationCode);

        assertFalse(result.getRoles().isEmpty());
        assertEquals(RoleName.ROLE_USER, result.getRoles().iterator().next().getRoleName());
        verify(this.userRepository).save(argThat(actUser -> {
            return actUser.getUsername().equals(username) &&
                    actUser.getRoles().size() == 1 &&
                    actUser.getRoles().iterator().next().getRoleName().equals(RoleName.ROLE_USER) &&
                    actUser.getPassword().equals(password) &&
                    actUser.getPublicId() != null;
        }));
    }

    @Test
    void saveUserAfterRegistration_WhenCacheUserDataDoesntExists_ThenThrowUserCacheDataNotFoundException() {
        var validationCode = new ValidationCodeDto("test@email.com", "123456");
        when(this.verificationCode.verificationRegistration(validationCode))
                .thenThrow(new UserCacheDataNotFoundException("Code invalid"));

        UserCacheDataNotFoundException exception = assertThrows(
                UserCacheDataNotFoundException.class,
                () -> this.authService.saveUserAfterRegistration(validationCode)
        );

        assertEquals("Code invalid", exception.getMessage());
        verifyNoInteractions(this.userRepository, this.userClientService);
    }

    @Test
    void login_WhenAuthenticationDataIsValid_ThenDoesntThrowsAuthenticationFailedException() {
        var username = "username";
        var password = "password";
        var authRequestDto = new AuthRequestDto(username, password);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        when(tokenService.getToken(username)).thenReturn(new JwtTokenDto("1234", "4321"));

        when(this.authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        JwtTokenDto result = this.authService.login(authRequestDto);

        assertNotNull(result);
        verify(this.tokenService).getToken(username);
        assertEquals("1234", result.getAccessToken());
        assertEquals("4321", result.getRefreshToken());
    }

    @Test
    void login_WhenInvalidCredentials_ThrowAuthenticationFailedException() {
        AuthRequestDto request = new AuthRequestDto("username", "password");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        AuthenticationFailedException exception = assertThrows(AuthenticationFailedException.class,
                () -> authService.login(request));

        assertEquals("Invalid username or password", exception.getMessage());
        verifyNoInteractions(this.tokenService);
    }
}










