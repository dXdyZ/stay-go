package com.lfey.authservice.service.clients;

import com.lfey.authservice.dto.UserDetailsDto;
import com.lfey.authservice.exception.ServerErrorException;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import feign.Request.HttpMethod;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserClientServiceTest {
    @Mock
    UserClient userClient;

    @InjectMocks
    UserClientService userClientService;

    private Request request;
    private Map<String, Collection<String>> responseHeaders;

    @BeforeEach
    void setUp() {
        // Инициализируем объект feign.Request
        request = Request.create(
                HttpMethod.GET, // HTTP-метод
                "http://example.com/users?email=test", // URL
                Collections.emptyMap(), // Заголовки запроса
                null, // Тело запроса (если есть)
                Charset.defaultCharset() // Кодировка
        );

        // Инициализируем заголовки ответа
        responseHeaders = Collections.emptyMap();
    }

    @Test
    void getUserByEmailFromUserService_WhenUserExist() {
        //given
        var email = "test@test.com";
        var userDto = UserDetailsDto.builder()
                .email(email)
                .build();
        doReturn(userDto).when(this.userClient).getUserByEmail(email);

        //when
        UserDetailsDto responseUserDetailsDto = userClientService.getUserByEmailFromUserService(email);

        //then
        assertEquals(email, responseUserDetailsDto.getEmail());
    }

    @Test
    void getUserByEmailFromUserService_WhenUserNotExist() {
        //given
        var email = "test@test.com";
        var userDto = UserDetailsDto.builder()
                .email(email)
                .build();

        doThrow(new FeignException.BadRequest("Bad Request", request, "Error response".getBytes(), responseHeaders))
                .when(this.userClient).getUserByEmail(email);

        //when
        var response = this.userClientService.getUserByEmailFromUserService(email);

        //then
        assertNull(response);
    }

    @Test
    void updateUsernameInUserService_WhenUserExistInTheUserService() {
        //given
        var newUsername = "test";
        var oldUsername = "test1";
        var userDto = UserDetailsDto.builder()
                .username(newUsername)
                .build();
        doReturn(userDto).when(this.userClient).updateUsername(newUsername, oldUsername);
        //when
        UserDetailsDto responseUserDetailsDto = userClientService.updateUsernameInUserService(newUsername, oldUsername);
        //then
        verify(this.userClient).updateUsername(newUsername, oldUsername);
        assertEquals(newUsername, responseUserDetailsDto.getUsername());
    }

    @Test
    void updateUsernameInUserService_WhenUserNotExistInTheUserService_ThrowServerErrorException() {
        //given
        var newUsername = "test";
        var oldUsername = "test1";

        doThrow(new FeignException.BadRequest("Bad Request", request,
                "Error response".getBytes(), responseHeaders))
                .when(this.userClient).updateUsername(newUsername, oldUsername);

        //when
        ServerErrorException exception = assertThrows(
                ServerErrorException.class,
                () -> userClientService.updateUsernameInUserService(newUsername, oldUsername)
        );

        //then
        verify(this.userClient).updateUsername(newUsername, oldUsername);
        assertEquals("An error occurred when updating the username", exception.getMessage());
    }

    @Test
    void updateUserEmailInUserService_WhenUserExistInTheUserService() {
        //given
        var newEmail = "test";
        var username = "test1";
        var userDto = UserDetailsDto.builder()
                .username(username)
                .email(newEmail)
                .build();
        doReturn(userDto).when(this.userClient).updateEmail(newEmail, username);
        //when
        UserDetailsDto responseUserDetailsDto = userClientService.updateUserEmailInUserService(newEmail, username);
        //then
        verify(this.userClient).updateEmail(newEmail, username);
        assertEquals(newEmail, responseUserDetailsDto.getEmail());
    }

    @Test
    void updateUserEmailInUserService_WhenUserExistInTheUserService_ThrowServerErrorException() {
        //given
        var newEmail = "test";
        var username = "test1";
        var userDto = UserDetailsDto.builder()
                .username(username)
                .email(newEmail)
                .build();
        doThrow(new FeignException.BadRequest("Bad Request", request,
                "Error response".getBytes(), responseHeaders))
                .when(this.userClient).updateEmail(newEmail, username);
        //when
        ServerErrorException exception = assertThrows(
                ServerErrorException.class,
                () -> userClientService.updateUserEmailInUserService(newEmail, username)
        );
        //then
        verify(this.userClient).updateEmail(newEmail, username);
        assertEquals("An error occurred when updating the email", exception.getMessage());
    }
}




