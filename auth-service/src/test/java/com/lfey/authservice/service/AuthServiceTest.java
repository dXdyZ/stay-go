package com.lfey.authservice.service;

import com.lfey.authservice.dto.AuthRequest;
import com.lfey.authservice.dto.JwtToken;
import com.lfey.authservice.dto.ValidationCode;
import com.lfey.authservice.dto.rabbit.EventType;
import com.lfey.authservice.entity.UserReg;
import com.lfey.authservice.exception.AuthenticationFailedException;
import com.lfey.authservice.exception.DuplicateUserException;
import com.lfey.authservice.repository.jpa.UserRepository;
import com.lfey.authservice.service.clients.UserClientService;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

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
        var userReg = UserReg.builder()
                .username(username)
                .password("password")
                .build();
        doReturn(false).when(this.userRepository).existsByUsername(username);
        doReturn(encodePassword).when(this.passwordEncoder).encode("password");

        this.authService.registerUser(userReg);

        verify(this.userRepository).existsByUsername(username);
        verify(this.passwordEncoder).encode("password");
        verify(this.generationCode).generateCode(userReg, EventType.REGISTRATION);
    }

    @Test
    void registerUser_WhenDuplicateUserExists_ThrowDuplicateUserException() {
        var username = "test";
        var userReg = UserReg.builder()
                .username(username)
                .password("password")
                .build();
        doReturn(true).when(this.userRepository).existsByUsername(username);

        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> this.authService.registerUser(userReg)
        );

        assertEquals(exception.getMessage(), String.format("The user named: %s already exists", userReg.getUsername()));
        verifyNoInteractions(this.passwordEncoder, this.generationCode);
    }

    @Test
    void getToken_ValidData() {
        var email = "test@test.com";
        var username = "test";
        var accessToke = "123456778dfdszxczxz";
        var refreshToken = "64532jgjfnv";
        var code = "123456";
        var userReg = UserReg.builder()
                .username(username)
                .email(email)
                .build();
        var validationCode = new ValidationCode(email, code);
        var jwtToken = new JwtToken(accessToke, refreshToken);
        doReturn(jwtToken).when(this.tokenService).getToken(username);
        doReturn(userReg).when(this.verificationCode).verification(validationCode);

        JwtToken response = this.authService.getJWT(validationCode);

        assertNotNull(response);
        assertEquals(accessToke, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());

        verify(this.tokenService).getToken(username);
        verify(this.userRepository).save(argThat(actUser -> {
            return actUser.getRoles().size() == 1 && actUser.getUsername().equals(username);
        }));
        verify(this.userClientService).userRegistrationInUserService(argThat(actUserDto -> {
            return actUserDto.getUsername().equals(username) &&
                    actUserDto.getEmail().equals(email);
        }));
    }

    @Test
    void login_ValidUserData() {
        var username = "test";
        var password = "password";
        var authRequest = new AuthRequest(username, password);
        var accessToken = "123456778dfdszxczxz";
        var refreshToken = "64532jgjfnv";
        var jwtToken = new JwtToken(accessToken, refreshToken);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                password,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username,
                password
        );

        doReturn(authentication).when(this.authenticationManager).authenticate(authToken);

        doReturn(jwtToken).when(this.tokenService).getToken(username);

        JwtToken response = this.authService.login(authRequest);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());

        verify(this.authenticationManager).authenticate(authToken);
        verify(this.tokenService).getToken(username);
    }


    @Test
    public void testLogin_ThrowsBadCredentialsException() {
        var username = "test";
        var password = "wrong_password";
        var authRequest = new AuthRequest(username, password);

        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        AuthenticationFailedException exception = assertThrows(
                AuthenticationFailedException.class,
                () -> authService.login(authRequest)
        );

        assertEquals("Invalid username or password", exception.getMessage());
        assertInstanceOf(AuthenticationFailedException.class, exception);
    }

    @Test
    void logout_ValidData() {
        var refreshToken = "123312312xxxxxdsda";

        this.authService.logout(refreshToken);

        verify(this.tokenService).deleteRefreshToken(refreshToken);
    }
}






