package com.lfey.authservice.oldTest.service;

import com.lfey.authservice.dto.JwtTokenDto;
import com.lfey.authservice.jwt.JwtUtils;
import com.lfey.authservice.service.security_service.TokenService;
import com.lfey.authservice.service.security_service.ValidationRefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    UserDetailsService userDetailsService;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    ValidationRefreshTokenService validationRefreshTokenService;

    @InjectMocks
    TokenService tokenService;

    @Test
    void getToken_WhenUserExists() {
        var username = "test";
        var password = "12345";
        var usersDetails = new User(username, password, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        var accessToken = "123344124324";
        var refreshToken = "12312312312";
        doReturn(usersDetails).when(this.userDetailsService).loadUserByUsername(username);
        doReturn(accessToken).when(this.jwtUtils).generationAccessToken(usersDetails);
        doReturn(refreshToken).when(this.jwtUtils).generationRefreshToken(usersDetails);

        JwtTokenDto response = tokenService.getToken(username);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        verify(this.userDetailsService).loadUserByUsername(username);
        verify(this.jwtUtils).generationRefreshToken(usersDetails);
        verify(this.jwtUtils).generationAccessToken(usersDetails);
        verify(this.validationRefreshTokenService).addValidToken(refreshToken, username);
    }

    @Test
    void getToken_WhenUserNotExists_ThrowUsernameNotFoundException() {
        var username = "test";
        doThrow(new UsernameNotFoundException("User not found")).when(this.userDetailsService).loadUserByUsername(username);

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> tokenService.getToken(username)
        );

        verifyNoInteractions(this.jwtUtils, this.validationRefreshTokenService);
    }

    @Test
    void deleteRefreshToken_ValidData() {
        var refreshToken = "12331231";

        this.tokenService.deleteRefreshToken(refreshToken);

        verify(this.validationRefreshTokenService).deleteToke(refreshToken);
    }
}





