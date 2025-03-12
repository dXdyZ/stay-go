package com.staygo.userservice.service;

import com.staygo.userservice.entity.Users;
import com.staygo.userservice.exception.DuplicateUserException;
import com.staygo.userservice.exception.UserNotFoundException;
import com.staygo.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void updateUsername_WhenDuplicateUserNoExists() {
        //given
        var oldName = "test1";
        var newName = "test";
        var user = Users.builder()
                .username(oldName)
                .build();
        doReturn(Optional.of(user)).when(this.userRepository).findByUsername(oldName);
        doReturn(false).when(this.userRepository).existsByUsername(newName);
        //when
        Users responseUser = this.userService.updateUsername(oldName, newName);
        //then
        verify(this.userRepository).findByUsername(oldName);
        verify(this.userRepository).existsByUsername(newName);
        verify(this.userRepository).save(argThat(actualUser -> {
            return actualUser.getUsername().equals(newName);
        }));
    }

    @Test
    void updateUsername_WhenDuplicateExists_ThrowDuplicateUserException() {
        //given
        var oldName = "test1";
        var newName = "test";
        var user = Users.builder()
                .username(oldName)
                .build();
        doReturn(Optional.of(user)).when(this.userRepository).findByUsername(oldName);
        doReturn(true).when(this.userRepository).existsByUsername(newName);
        //when
        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> userService.updateUsername(oldName, newName)
        );
        //then
        assertEquals(String.format("User with name: %s already exists", newName), exception.getMessage());
        verify(this.userRepository).findByUsername(oldName);
        verify(this.userRepository).existsByUsername(newName);
        verify(this.userRepository, never()).save(new Users());
    }

    @Test
    void updateUsername_WhenUserNotExists_ThrowUserNotFoundException() {
        //given
        var oldName = "test1";
        var newName = "test";
        doReturn(Optional.empty()).when(this.userRepository).findByUsername(oldName);
        //when
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUsername(oldName, newName)
        );
        //then
        assertEquals(String.format("User by username: %s not found", oldName), exception.getMessage());
        verify(this.userRepository).findByUsername(oldName);
        verify(this.userRepository, never()).existsByUsername(newName);
        verify(this.userRepository, never()).save(new Users());
    }

    @Test
    void updatePhoneNumber_ValidData() {
        //given
        var username = "test1";
        var newNumber = "12345";
        var user = Users.builder()
                .username(username)
                .phoneNumber("54321")
                .build();
        doReturn(Optional.of(user)).when(this.userRepository).findByUsername(username);
        //when
        Users responseUser = this.userService.updatePhoneNumber(username, newNumber);
        //then
        assertEquals(newNumber, responseUser.getPhoneNumber());
        verify(this.userRepository).save(argThat(actualUser -> {
            return actualUser.getPhoneNumber().equals(newNumber);
        }));
    }

    @Test
    void updateEmail_WhenDuplicateUserNotExists() {
        //given
        var username = "test1";
        var newEmail = "test@tst.com";
        var oldEmail = "test@test.com";
        var user = Users.builder()
                .username(username)
                .email(oldEmail)
                .build();
        doReturn(Optional.of(user)).when(this.userRepository).findByUsername(username);
        doReturn(false).when(this.userRepository).existsByEmail(newEmail);
        //when
        Users responseUser = this.userService.updateEmail(username, newEmail);
        //then
        assertEquals(newEmail, responseUser.getEmail());
        verify(this.userRepository).existsByEmail(newEmail);
        verify(this.userRepository).save(argThat(actualUser -> {
            return actualUser.getEmail().equals(newEmail);
        }));
        verify(this.userRepository).findByUsername(username);
    }

    @Test
    void updateEmail_WhenDuplicateUserExists_ThrowDuplicateUserException() {
        //given
        var username = "test1";
        var newEmail = "test@tst.com";
        var oldEmail = "test@test.com";
        var user = Users.builder()
                .username(username)
                .email(oldEmail)
                .build();
        doReturn(Optional.of(user)).when(this.userRepository).findByUsername(username);
        doReturn(true).when(this.userRepository).existsByEmail(newEmail);
        //when
        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> userService.updateEmail(username, newEmail)
        );
        //then
        assertEquals(String.format("User with email: %s already exists", newEmail), exception.getMessage());
        verify(this.userRepository).findByUsername(username);
        verify(this.userRepository).existsByEmail(newEmail);
        verify(this.userRepository, never()).save(user);
    }
}




