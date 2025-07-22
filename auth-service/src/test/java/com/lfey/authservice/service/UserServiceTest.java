package com.lfey.authservice.service;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.dto.kafka.EventType;
import com.lfey.authservice.entity.jpa.Role;
import com.lfey.authservice.entity.jpa.RoleName;
import com.lfey.authservice.entity.redis.UserRegistration;
import com.lfey.authservice.entity.jpa.Users;
import com.lfey.authservice.exception.DuplicateRoleException;
import com.lfey.authservice.exception.DuplicateUserException;
import com.lfey.authservice.exception.UserNotFoundException;
import com.lfey.authservice.repository.jpaRepository.UserRepository;
import com.lfey.authservice.service.clients.UserClientService;
import com.lfey.authservice.service.verification.GenerationCode;
import com.lfey.authservice.service.verification.VerificationCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    GenerationCode generationCode;

    @Mock
    VerificationCode verificationCode;

    @Mock
    UserClientService userClientService;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    void updateEmail_WhenDuplicateUserNotExists() {
        //given
        var email = "test@test.com";
        var username = "test";
        var emailUpdate = new EmailUpdateDto(email);
        doReturn(null).when(this.userClientService).getUserByEmailFromUserService(email);
        //when
        userService.updateEmail(emailUpdate, username);
        //then
        verify(this.userClientService).getUserByEmailFromUserService(email);
        verify(this.generationCode).generateCode(
                UserRegistration.builder()
                        .email(email)
                        .username(username)
                        .build(), EventType.EMAIL_RESET
        );
    }

    @Test
    void updateEmail_WhenDuplicateUserExists_ThrowDuplicateUserException() {
        //given
        var email = "test@test.com";
        var username = "test";
        var emailUpdate = new EmailUpdateDto(email);
        doReturn(new UserDetailsDto()).when(this.userClientService).getUserByEmailFromUserService(email);
        //when
        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> this.userService.updateEmail(emailUpdate, username)
        );
        //then
        assertEquals(String.format("User with email: %s already exists", emailUpdate.email()), exception.getMessage());
        verifyNoInteractions(this.generationCode);
    }

    @Test
    void updateEmailInUserService_ValidData() {
        //given
        var username = "test";
        var email = "test@test.com";
        var code = "123456";
        var validationCode = new ValidationCodeDto(email, code);
        var userReg = UserRegistration.builder()
                .email(email)
                .username(username)
                .code(code)
                .build();
        var userDto = UserDetailsDto.builder()
                .username(username)
                .email(email)
                .build();
        doReturn(userReg).when(this.verificationCode).verificationRegistration(validationCode);
        doReturn(userDto).when(this.userClientService).updateUserEmailInUserService(email, username);
        //when
        UserDetailsDto response = this.userService.updateEmailInUserService(validationCode, username);
        //then
        assertNotNull(response);
        verify(this.userClientService).updateUserEmailInUserService(email, username);
        verify(this.verificationCode).verificationRegistration(validationCode);
    }

    @Test
    void updateUsername_WhenDuplicateUserNotExists() {
        var oldUsername = "test";
        var newUsername = "newTest";
        var userNameUpdate = new UsernameUpdateDto(newUsername);
        var userDto = UserDetailsDto.builder()
                .username(newUsername)
                .build();
        var users = Users.builder()
                .username(oldUsername)
                .build();
        doReturn(Optional.empty()).when(this.userRepository).findByUsername(newUsername);
        doReturn(userDto).when(this.userClientService).updateUsernameInUserService(newUsername, oldUsername);
        doReturn(Optional.of(users)).when(this.userRepository).findByUsername(oldUsername);
        //when
        UserDetailsDto response = userService.updateUsername(oldUsername, userNameUpdate);
        //then
        assertNotNull(response);
        verify(this.userRepository).findByUsername(newUsername);
        verify(this.userRepository).findByUsername(oldUsername);
        verify(this.userRepository).save(argThat(actUser -> {
            return actUser.getUsername().equals(newUsername);
        }));
        verify(this.userClientService).updateUsernameInUserService(newUsername, oldUsername);
    }

    @Test
    void updateUsername_WhenDuplicateUserExists_ThrowDuplicateUserException() {
        //give
        var oldUsername = "test";
        var newUsername = "test1";
        var usernameUpdate = new UsernameUpdateDto(newUsername);
        doReturn(Optional.of(new Users())).when(this.userRepository).findByUsername(newUsername);
        //then
        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> this.userService.updateUsername(oldUsername, usernameUpdate)
        );
        //then
        assertEquals(String.format("User with username: %s already exists", usernameUpdate.newUsername()), exception.getMessage());
        verifyNoInteractions(this.userClientService);
        verify(this.userRepository, never()).save(new Users());
    }

    @Test
    void resetPassword_WhenUserExists() {
        var password = "12345";
        var username = "test";
        var email = "test@test.com";
        var resetPassword = new ResetPasswordRequestDto(password);
        var users = Users.builder()
                .username(username)
                .password("oldEncodePassword")
                .build();
        var userDto = UserDetailsDto.builder()
                .email(email)
                .build();
        var userReg = UserRegistration.builder()
                .email(email)
                .password("encode12345")
                .build();
        doReturn(Optional.of(users)).when(this.userRepository).findByUsername(username);
        doReturn(userDto).when(this.userClientService).getUserByUsernameFromUserService(username);
        doReturn("encode12345").when(this.passwordEncoder).encode(password);
        //when
        this.userService.resetPassword(resetPassword, username);
        //then
        verify(this.userRepository).findByUsername(username);
        verify(this.userClientService).getUserByUsernameFromUserService(username);
        verify(this.passwordEncoder).encode(password);
        verify(this.generationCode).generateCode(userReg, EventType.PASSWORD_RESET);
    }

    @Test
    void resetPassword_WhenUserNotExists_ThrowUserNotFoundException() {
        var username = "test";
        var resetPass = new ResetPasswordRequestDto("12345");
        doReturn(Optional.empty()).when(this.userRepository).findByUsername(username);
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> this.userService.resetPassword(resetPass, username)
        );
        assertEquals(String.format("User by name: %s not found", username), exception.getMessage());
        verifyNoInteractions(this.userClientService, this.generationCode);
    }

    @Test
    void addRole_WhenUserExistsRoleNotDuplicated() {
        var username = "test";
        var users = Users.builder()
                .username(username)
                .build();
        users.getRoles().add(Role.builder().roleName(RoleName.ROLE_USER).build());
        var roleRequest = new RoleRequestDto(RoleName.ROLE_ADMIN);
        doReturn(Optional.of(users)).when(this.userRepository).findByUsername(username);

        this.userService.addRole(username, roleRequest);

        verify(this.userRepository).findByUsername(username);
        verify(this.userRepository).save(argThat(actUser -> {
            return actUser.getRoles().stream()
                    .anyMatch(role -> role.getRoleName().equals(RoleName.ROLE_ADMIN));
        }));
    }

    @Test
    void addRole_WhenUserExistsRoleDuplicated_ThrowDuplicateRoleException() {
        var username = "test";
        var users = Users.builder()
                .username(username)
                .build();
        users.getRoles().add(Role.builder().roleName(RoleName.ROLE_USER).build());
        var roleRequest = new RoleRequestDto(RoleName.ROLE_USER);
        doReturn(Optional.of(users)).when(this.userRepository).findByUsername(username);

        DuplicateRoleException exception = assertThrows(
                DuplicateRoleException.class,
                () -> this.userService.addRole(username, roleRequest)
        );

        assertEquals(String.format("Role: %s is already assigned to the user", roleRequest.role()), exception.getMessage());
        verify(this.userRepository, never()).save(argThat(actUser -> {
            return actUser.getRoles().size() != 2;
        }));
    }

    @Test
    void deleteRoleUser_ValidData() {
        var username = "test";
        var users = Users.builder()
                .username(username)
                .build();
        users.getRoles().add(Role.builder().roleName(RoleName.ROLE_USER).build());
        users.getRoles().add(Role.builder().roleName(RoleName.ROLE_ADMIN).build());
        var roleRequest = new RoleRequestDto(RoleName.ROLE_USER);

        doReturn(Optional.of(users)).when(this.userRepository).findByUsername(username);

        this.userService.deleteRoleUser(username, roleRequest);

        verify(this.userRepository).save(argThat(actUser -> {
            return actUser.getRoles().size() == 1;
        }));
    }

    @Test
    void getUserByName_WhenUserExists() {
        var username = "test";
        var users = Users.builder()
                .username(username)
                .build();
        doReturn(Optional.of(users)).when(this.userRepository).findByUsername(username);

        Users response = this.userService.getUserByName(username);

        assertNotNull(response);
        assertEquals(username, response.getUsername());
        verify(this.userRepository).findByUsername(username);
    }

    @Test
    void getUserByName_WhenUserNotExists_ThrowUserNotFoundException() {
        var username = "test";
        doReturn(Optional.empty()).when(this.userRepository).findByUsername(username);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> this.userService.getUserByName(username)
        );

        assertEquals(String.format("User by name: %s not found", username), exception.getMessage());
    }
}









