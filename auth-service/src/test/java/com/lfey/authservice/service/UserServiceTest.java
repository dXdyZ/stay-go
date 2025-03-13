package com.lfey.authservice.service;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.dto.rabbit.EventType;
import com.lfey.authservice.entity.Role;
import com.lfey.authservice.entity.RoleName;
import com.lfey.authservice.entity.Users;
import com.lfey.authservice.exception.DuplicateRoleException;
import com.lfey.authservice.exception.DuplicateUserException;
import com.lfey.authservice.exception.ServerErrorException;
import com.lfey.authservice.exception.UserNotFoundException;
import com.lfey.authservice.repository.jpa.UserRepository;
import com.lfey.authservice.service.clients.UserClientService;
import com.lfey.authservice.service.verification.GenerationCode;
import com.lfey.authservice.service.verification.VerificationCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserClientService userClientService;

    @Mock
    GenerationCode generationCode;

    @Mock
    VerificationCode verificationCode;

    @Mock
    RegistrationEventDispatcher registrationEventDispatcher;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("Выполнение метода с существующим пользователем и корректным именем")
    void getUserByName_ReturnValidUser() {
        //given
        var user = Users.builder()
                .id(1L)
                .username("test")
                .password("1234")
                .createAt(Instant.now())
                .roles(Collections.singleton(Role.builder()
                                .id(1L)
                                .roleName(RoleName.ROLE_USER)
                        .build()))
                .build();
        doReturn(Optional.of(user)).when(this.userRepository).findByUsername("test");

        //when
        var userFromUserService = this.userService.getUserByName("test");
        //then
        assertNotNull(userFromUserService);
        assertInstanceOf(Users.class, userFromUserService);
        assertEquals(userFromUserService.getUsername(), "test");
    }

    @Test
    @DisplayName("Выполнения метод с несуществующем метод, проверка выдачи правильного исключения")
    void getUserByName_ReturnValidException() {
        //given
        var username = "test";
        doReturn(Optional.empty()).when(this.userRepository).findByUsername("test");
        //when /then
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserByName(username)
        );

        assertEquals("User by name: test not found", exception.getMessage());
    }

    @Test
    @DisplayName("Корректный вызов метода, когда передаваемого пользователя не существует")
    void registerUser_WhenUserDoesNotExist_RegistersUser() {
        //given
        var username = "test";
        var email = "test@test.com";
        var password = "1234";
        var userReg = UserReg.builder()
                .email("test@test.com")
                .username("test")
                .password("1234")
                .phoneNumber("12345")
                .build();
        doReturn(false).when(this.userRepository).existsByUsername(username);
        doReturn("password").when(this.passwordEncoder).encode(password);

        //when
        userService.registerUser(userReg);

        //then verify - проверяет был ли вызван метод
        verify(this.userRepository).existsByUsername(username);
        verify(this.passwordEncoder).encode(password);
        verify(this.generationCode).generateCode(userReg, EventType.REGISTRATION);
    }

    @Test
    @DisplayName("Вызов метода, с дупликацией пользователя")
    void registerUser_WhenUsernameExists_ThrowsDuplicateUserException() {
        //given
        var username = "test";
        var email = "test@test.com";
        var password = "1234";
        var userReg = UserReg.builder()
                .email("test@test.com")
                .username("test")
                .password("1234")
                .phoneNumber("12345")
                .build();

        doReturn(true).when(this.userRepository).existsByUsername(username);

        //when
        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> userService.registerUser(userReg)
        );

        //then
        assertEquals(String.format("The user named: %s already exists", username), exception.getMessage());
        verify(userRepository).existsByUsername(username);
        verifyNoInteractions(this.userClientService, this.passwordEncoder, this.generationCode);
    }

    @Test
    void saveUserAfterRegistration_ValidData() {
        //given
        var username = "test";
        var email = "test@test.com";
        var password = "1234";
        var validationCode = new ValidationCode(email, "123456");
        var userReg = UserReg.builder()
                .email("test@test.com")
                .username(username)
                .password(password)
                .phoneNumber("12345")
                .build();
        var user = Users.builder()
                .username(username)
                .password(password)
                .build();
        var userDto = UserDto.builder()
                .email(email)
                .phoneNumber("12345")
                .username(username)
                .build();
        doReturn(userReg).when(this.verificationCode).verification(validationCode);
        //when
        this.userService.saveUserAfterRegistration(validationCode);
        //then
        verify(this.verificationCode).verification(validationCode);

        //Проверка только нужных аргументов
        verify(this.userRepository).save(argThat(actualUser -> {
            return actualUser.getUsername().equals(username) &&
                    actualUser.getPassword().equals(password) &&
                    actualUser.getRoles().stream()
                            .anyMatch(role -> role.getRoleName().equals(RoleName.ROLE_USER));
        }));

        verify(this.userClientService).userRegistrationInUserService(userDto);
    }

    @Test
    @DisplayName("Вызов метода на существующего пользователя")
    void resetPassword_WhenUsernameExists() {
        //given
        var username = "test";
        var password = "password";
        var user = Optional.of(Users.builder()
                .username(username)
                .password("1234")
                .id(1L)
                .build());
        var resetPassword = new ResetPasswordRequest(password);
        doReturn(password).when(this.passwordEncoder).encode(password);
        doReturn(user).when(this.userRepository).findByUsername(username);

        //when
        this.userService.resetPassword(resetPassword, username);

        //then
        verify(this.passwordEncoder).encode(password);
        verify(this.userRepository).findByUsername(username);
        assertEquals(password, user.get().getPassword());
    }

    @Test
    @DisplayName("Вызов метода на не существующего пользователя")
    void resetPassword_WhenUserNotExists_ThrowsUserNotFoundException() {
        //given
        var name = "test";
        var resetPassword = new ResetPasswordRequest("hello");
        //when
        doReturn(Optional.empty()).when(this.userRepository).findByUsername(name);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> this.userService.resetPassword(resetPassword, name)
        );

        //then
        assertEquals(String.format("User by name: %s not found", name), exception.getMessage());
        verify(this.userRepository).findByUsername(name);
        verify(this.userRepository, never()).save(new Users());
        verifyNoInteractions(this.passwordEncoder);
    }

    @Test
    @DisplayName("Вызов метода с уникальным email")
    void updateEmail_WhenUserWithUpdateEmailDoesNotExists() {
        //given
        var username = "test";
        var email = "hello@hello.com";
        var emailReset = new EmailUpdate(email);
        var userReg = UserReg.builder()
                        .email(email)
                        .username(username)
                        .build();

        doReturn(null).when(this.userClientService).getUserByEmailFromUserService(email);

        //when
        this.userService.updateEmail(emailReset, username);

        //then
        verify(this.userClientService).getUserByEmailFromUserService(email);
        verify(this.generationCode).generateCode(userReg, EventType.EMAIL_RESET);
    }

    @Test
    void updateEmail_WhenUserWithUpdateEmailExists_ThrowsDuplicateUserException() {
        //given
        var username = "test";
        var email = "hello@hello.com";
        var emailUpdate = new EmailUpdate(email);

        doReturn(new UserDto()).when(this.userClientService).getUserByEmailFromUserService(email);

        //when
        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> this.userService.updateEmail(emailUpdate, username)
        );

        //then
        assertEquals(String.format("User with email: %s already exists", email), exception.getMessage());
        verify(this.userClientService).getUserByEmailFromUserService(email);
        verifyNoInteractions(this.generationCode);
    }

    @Test
    void updateEmailInUserService_ValidData() {
        //given
        var username = "test";
        var email = "test@test.com";
        var validationCode = new ValidationCode(email, "123456");
        var userReg = UserReg.builder()
                .email(email)
                .username(username)
                .build();
        var userDto = UserDto.builder()
                .email(email)
                .username(username)
                .build();
        doReturn(userReg).when(this.verificationCode).verification(validationCode);
        doReturn(userDto).when(this.userClientService).updateUserEmailInUserService(email, username);

        //when
        var retutnUserDto = this.userService.updateEmailInUserService(validationCode, username);

        //then
        verify(this.verificationCode).verification(validationCode);
        verify(this.userClientService).updateUserEmailInUserService(email, username);
        assertInstanceOf(UserDto.class, retutnUserDto);
        assertEquals(email, retutnUserDto.getEmail());
    }


    @Test
    void updateUsername_WhenUserByNewUsernameNotExists() {
        //given
        var oldUsername = "test1";
        var username = "test";
        var updateEmail = new UsernameUpdate(username);
        var userDto = UserDto.builder()
                .username(username)
                .build();
        var user = Optional.of(Users.builder()
                        .username(oldUsername)
                .build());

        doReturn(Optional.empty()).when(this.userRepository).findByUsername(username);
        doReturn(user).when(this.userRepository).findByUsername(oldUsername);
        doReturn(userDto).when(this.userClientService).updateUsernameInUserService(username, oldUsername);

        //when
        var returnUsed = this.userService.updateUsername(oldUsername, updateEmail);

        //then
        verify(this.userRepository).findByUsername(username);
        verify(this.userRepository).findByUsername(oldUsername);
        verify(this.userClientService).updateUsernameInUserService(username, oldUsername);
        verify(this.userRepository).save(argThat(actualUser -> {
            return actualUser.getUsername().equals(username);
        }));
        assertEquals(username, returnUsed.getUsername());
    }

    @Test
    void updateUsername_WhenUserByNewUsernameExists_ThrowsDuplicateExceptio() {
        //given
        var oldUsername = "test";
        var newUsername = "test1";
        var updateUsername = new UsernameUpdate(newUsername);

        doReturn(Optional.of(new Users())).when(this.userRepository).findByUsername(newUsername);

        //when
        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> userService.updateUsername(oldUsername, updateUsername)
        );

        //then
        assertEquals(String.format("User with username: %s already exists", updateUsername.newUsername()),
                exception.getMessage());
        verify(this.userRepository, never()).save(new Users());
        verify(this.userRepository, never()).findByUsername(oldUsername);
    }

    @Test
    void addRole_WhenNotDuplicateRole() {
        //given
        var username = "test";
        var roleName = "ROLE_ADMIN";
        var addRoleRequest = new AddRoleRequest(roleName);
        var user = Users.builder()
                .id(1L)
                .username(username)
                .build();
        user.getRoles().add(Role.builder()
                        .id(1L)
                        .roleName(RoleName.ROLE_USER)
                .build());
        doReturn(Optional.of(user)).when(this.userRepository).findByUsername(username);

        //when
        this.userService.addRole(username, addRoleRequest);

        //then
        verify(this.userRepository).save(argThat(actualUser -> {
            return actualUser.getRoles().size() == 2;
        }));
    }

    @Test
    void addRole_WhenDuplicateRole_ThrowsDuplicateRoleException() {
        // given
        var username = "test";
        var roleName = "ROLE_USER";
        var addRoleRequest = new AddRoleRequest(roleName);

        // Создаем пользователя с уже существующей ролью
        var user = Users.builder()
                .id(1L)
                .username(username)
                .roles(new HashSet<>()) // Инициализируем roles как изменяемый Set
                .build();
        user.getRoles().add(Role.builder()
                .id(1L)
                .roleName(RoleName.ROLE_USER)
                .build());

        doReturn(Optional.of(user)).when(this.userRepository).findByUsername(username);

        // when
        DuplicateRoleException exception = assertThrows(
                DuplicateRoleException.class,
                () -> userService.addRole(username, addRoleRequest)
        );

        // then
        verify(this.userRepository, never()).save(any()); // Проверяем, что save не вызывался
        assertEquals(
                String.format("Role: %s is already assigned to the user", addRoleRequest.role()),
                exception.getMessage()
        );
    }

    @Test
    void deleteRoleUser() {
        //give
        var username = "test";
        var roleName = "ROLE_ADMIN";

        var user = Users.builder()
                .id(1L)
                .username(username)
                .roles(new HashSet<>())
                .build();
        user.getRoles().add(Role.builder()
                .id(1L)
                .roleName(RoleName.ROLE_USER)
                .build());
        user.getRoles().add(Role.builder()
                .id(2L)
                .roleName(RoleName.ROLE_ADMIN)
                .build());

        doReturn(Optional.of(user)).when(this.userRepository).findByUsername(username);

        //when
        Users users = this.userService.deleteRoleUser(username, roleName);

        //then
        assertEquals(Role.builder().id(1L).roleName(RoleName.ROLE_USER).build(), users.getRoles().iterator().next());
        assertEquals(1, users.getRoles().size());
        assertFalse(users.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals(RoleName.ROLE_ADMIN)));
    }
}








