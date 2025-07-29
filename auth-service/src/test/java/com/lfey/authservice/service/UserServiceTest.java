package com.lfey.authservice.service;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.dto.kafka.EventType;
import com.lfey.authservice.entity.jpa.Role;
import com.lfey.authservice.entity.jpa.RoleName;
import com.lfey.authservice.entity.jpa.Users;
import com.lfey.authservice.entity.redis.EmailUpdate;
import com.lfey.authservice.exception.DuplicateRoleException;
import com.lfey.authservice.exception.DuplicateUserException;
import com.lfey.authservice.repository.jpaRepository.UserRepository;
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

import java.util.Optional;
import java.util.UUID;

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
    private UserService userService;


    @Test
    @DisplayName("Система должна генерировать код подтверждения при попытке изменить email на не занятый")
    void updateEmail_WhenUserByEmailDoesntDuplicate() {
        var email = "test@emil.com";
        var publicId = UUID.randomUUID();
        var emailUpdate = new EmailUpdateDto(email);

        when(this.userClientService.getUserByEmailFromUserService(email)).thenReturn(null);

        this.userService.updateEmail(emailUpdate, publicId);

        verify(this.generationCode).generateCode(EmailUpdate.builder()
                        .newEmail(email)
                        .publicId(publicId)
                .build());
    }

    @Test
    @DisplayName("Система не должна генерировать код подтверждения при попытке изменить email на занятый, а также должна выбросить исключение о попытке дублирования email")
    void updateEmail_WhenUserByEmailDuplicate_ThenThrowsDuplicateUserException() {
        var email = "test@emil.com";
        var publicId = UUID.randomUUID();
        var emailUpdate = new EmailUpdateDto(email);

        when(this.userClientService.getUserByEmailFromUserService(email)).thenReturn(new UserDetailsDto());

        DuplicateUserException exception = assertThrows(DuplicateUserException.class,
                () -> this.userService.updateEmail(emailUpdate, publicId));

        verifyNoInteractions(this.generationCode);
        assertEquals("User with email: %s already exists".formatted(email), exception.getMessage());
    }

    @Test
    @DisplayName("Система должна обновить username при попытке обновления на не занятый username")
    void updateUsername_WhenUserByUsernameDoesntDuplicate() {
        var username = "username";
        var publicId = UUID.randomUUID();
        var usernameUpdate = new UsernameUpdateDto(username);
        var userDetailsDto = new UserDetailsDto(username, "8923232323", "email@email.com");
        var users = Users.builder()
                .username("test")
                .publicId(publicId)
                .build();

        when(this.userRepository.existsByUsername(username)).thenReturn(false);
        when(this.userClientService.updateUsernameInUserService(username, publicId)).thenReturn(userDetailsDto);
        when(this.userRepository.findByPublicId(publicId)).thenReturn(Optional.of(users));

        UserDetailsDto result = this.userService.updateUsername(publicId, usernameUpdate);

        assertNotNull(result);
        verify(this.userClientService).updateUsernameInUserService(username, publicId);
        verify(this.userRepository).findByPublicId(publicId);
        verify(this.userRepository).save(argThat(actUser -> {
            return actUser.getUsername().equals(username); }));
    }

    @Test
    @DisplayName("Система не должна обновлять username при попытке обновления на занятый username, а также должна выбросить исключение о попытке дублирования username")
    void updateUsername_WhenUserByUsernameDuplicate_ThenThrowsDuplicateUserException() {
        var publicId = UUID.randomUUID();
        var username = "username";
        var usernameUpdateDto = new UsernameUpdateDto(username);

        when(this.userRepository.existsByUsername(username)).thenReturn(true);

        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> this.userService.updateUsername(publicId, usernameUpdateDto)
        );

        assertEquals(
                "User with username: %s already exists".formatted(username),
                exception.getMessage()
        );

        verifyNoInteractions(this.userClientService);

        verify(this.userRepository).existsByUsername(username);
    }

    @Test
    @DisplayName("Обновление пароля")
    void resetPassword_WhenValidData() {
        var password = "password";
        var publicId = UUID.randomUUID();
        var email = "email@email.com";
        var userDetails = UserDetailsDto.builder()
                .email(email)
                .build();

        when(this.passwordEncoder.encode("password")).thenReturn("encodePassword");
        when(this.userClientService.getUserByPublicIdFromUserService(publicId)).thenReturn(userDetails);

        this.userService.resetPassword(new ResetPasswordRequestDto(password), publicId);

        verify(this.userClientService).getUserByPublicIdFromUserService(publicId);
        verify(this.generationCode).generateCode(argThat(actRegUser ->
                actRegUser.getEmail().equals(email) && actRegUser.getPassword().equals("encodePassword")
        ), eq(EventType.PASSWORD_RESET));
    }

    @Test
    @DisplayName("Система должна добавить роль пользователю если она не дублируется")
    void addRole_WhenDoesntDuplicateRole() {
        var username = "username";
        var role = RoleName.ROLE_ADMIN;
        var roleRequest = new RoleRequestDto(role);

        var users = Users.builder()
                .username(username)
                .build();

        when(this.userRepository.findByUsername(username)).thenReturn(Optional.of(users));

        this.userService.addRole(username, roleRequest);

        verify(this.userRepository).findByUsername(username);
        verify(this.userRepository).save(argThat(actUser ->
                actUser.getRoles().size() == 1 && actUser.getRoles().iterator().next().getRoleName().equals(RoleName.ROLE_ADMIN)));
    }

    @Test
    @DisplayName("Система не должна добавлять роль пользователю при ее дублировании")
    void addRole_WhenDuplicateRole_ThenThrowDuplicateRoleException() {
        var username = "username";
        var role = RoleName.ROLE_ADMIN;
        var roleRequest = new RoleRequestDto(role);

        var users = Users.builder()
                .username(username)
                .build();
        users.getRoles().add(Role.builder().roleName(RoleName.ROLE_ADMIN).build());

        when(this.userRepository.findByUsername(username)).thenReturn(Optional.of(users));

        DuplicateRoleException exception = assertThrows(DuplicateRoleException.class,
                () -> this.userService.addRole(username, roleRequest));

        verify(this.userRepository, never()).save(any());
        assertEquals("Role: %s is already assigned to the user".formatted(role), exception.getMessage());
    }

    @Test
    @DisplayName("Система должна удалить указанную роль у пользователя при ее существовании")
    void deleteRole_WhenValidData() {
        var username = "username";
        var roleName = RoleName.ROLE_USER;
        var roleRequest = new RoleRequestDto(roleName);
        var users = Users.builder()
                .username(username)
                .build();
        users.getRoles().add(Role.builder().roleName(RoleName.ROLE_USER).build());

        when(this.userRepository.findByUsername(username)).thenReturn(Optional.of(users));

        this.userService.deleteRoleUser(username, roleRequest);

        verify(this.userRepository).save(argThat(actUser -> actUser.getRoles().isEmpty()));
    }

    @Test
    void updateEmailInUserService_WhenUserExistsInTheCache() {
        var email = "email@email.com";
        var code = "123456";
        var publicId = UUID.randomUUID();
        var validationCodeDto = new ValidationCodeDto(email, code);
        var emailUpdate = new EmailUpdate(email, publicId, code);
        var userDetail = UserDetailsDto.builder()
                .email(email)
                .build();


        when(this.verificationCode.verificationEmailUpdate(validationCodeDto)).thenReturn(emailUpdate);
        when(this.userClientService.updateUserEmailInUserService(email, publicId)).thenReturn(userDetail);

        UserDetailsDto result = this.userService.updateEmailInUserService(validationCodeDto, publicId);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(this.verificationCode).verificationEmailUpdate(validationCodeDto);
        verify(this.userClientService).updateUserEmailInUserService(email, publicId);
    }
}












