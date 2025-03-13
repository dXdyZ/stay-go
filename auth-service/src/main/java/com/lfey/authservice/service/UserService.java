package com.lfey.authservice.service;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.dto.rabbit.EventType;
import com.lfey.authservice.entity.Role;
import com.lfey.authservice.entity.RoleName;
import com.lfey.authservice.entity.Users;
import com.lfey.authservice.exception.*;
import com.lfey.authservice.repository.jpa.UserRepository;
import com.lfey.authservice.service.clients.UserClientService;
import com.lfey.authservice.service.verification.GenerationCode;
import com.lfey.authservice.service.verification.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GenerationCode generationCode;
    private final VerificationCode verificationCode;
    private final UserClientService userClientService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       GenerationCode generationCode, VerificationCode verificationCode, UserClientService userClientService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.generationCode = generationCode;
        this.verificationCode = verificationCode;
        this.userClientService = userClientService;
    }


    //TODO Добавить проверку уникальности почты и изменить тест
    @Transactional
    public void registerUser(UserReg userReg) throws DuplicateUserException{
        if (!userRepository.existsByUsername(userReg.getUsername())) {
            userReg.setPassword(passwordEncoder.encode(userReg.getPassword()));
            generationCode.generateCode(userReg, EventType.REGISTRATION);
        } else throw new DuplicateUserException(String.format("The user named: %s already exists", userReg.getUsername()));
    }

    @Transactional
    public JwtToken getJWT(ValidationCode validationCode) throws UserCacheDataNotFoundException, InvalidCodeException {
        saveUserAfterRegistration(validationCode);
        //TODO Сделать генерацию и возврат токена пользователю
        return null;
    }

    @Transactional
    public void updateEmail(EmailUpdate emailUpdate, String username) throws DuplicateUserException{
        //Может стоит обновить проверку на null через метод Objects.requireNonNull()
        UserDto userDto = userClientService.getUserByEmailFromUserService(emailUpdate.email());
        if (userDto == null) {
            generationCode.generateCode(UserReg.builder()
                    .email(emailUpdate.email())
                    .username(username)
                    .build(), EventType.EMAIL_RESET);
        } else throw new DuplicateUserException(String.format("User with email: %s already exists", emailUpdate.email()));
    }

    @Transactional
    public void saveUserAfterRegistration(ValidationCode validationCode) {
        UserReg userReg = verificationCode.verification(validationCode);
        Users users = Users.builder()
                .username(userReg.getUsername())
                .password(userReg.getPassword())
                .build();
        users.getRoles().add(Role.builder().roleName(RoleName.ROLE_USER).build());
        userRepository.save(users);
        userClientService.userRegistrationInUserService(UserDto.builder()
                .email(userReg.getEmail())
                .phoneNumber(userReg.getPhoneNumber())
                .username(userReg.getUsername())
                .build());
    }

    public UserDto updateEmailInUserService(ValidationCode validationCode, String username) throws ServerErrorException {
        UserReg userReg = verificationCode.verification(validationCode);
        return userClientService.updateUserEmailInUserService(userReg.getEmail(), username);
    }

    //TODO В дальнейшем сделать отзыв токена что бы не было исключений NullPointerException
    @Transactional
    public UserDto updateUsername(String username, UsernameUpdate usernameUpdate) throws ServerErrorException,
            DuplicateUserException{
        if (userRepository.findByUsername(usernameUpdate.newUsername()).orElse(null) == null) {
            UserDto userDto = userClientService.updateUsernameInUserService(usernameUpdate.newUsername(), username);
            Users users = userRepository.findByUsername(username).get();
            users.setUsername(userDto.getUsername());
            userRepository.save(users);
            return userDto;
        } else throw new DuplicateUserException(
                String.format("User with username: %s already exists", usernameUpdate.newUsername()));
    }

    @Transactional
    public void addRole(String username, AddRoleRequest addRoleRequest) throws UserNotFoundException, DuplicateRoleException{
        Users users = getUserByName(username);
        boolean status = users.getRoles().add(Role.builder().roleName(RoleName.valueOf(addRoleRequest.role())).build());
        if (status) {
            userRepository.save(users);
        } else {
            throw new DuplicateRoleException(
                    String.format("Role: %s is already assigned to the user", addRoleRequest.role()));
        }
    }

    @Transactional
    public Users deleteRoleUser(String username, String role) throws UserNotFoundException{
        Users users = getUserByName(username);
        Set<Role> updateRole = users.getRoles().stream()
                .filter(roleUpdate -> !roleUpdate.getRoleName().equals(RoleName.valueOf(role)))
                .collect(Collectors.toSet());
        users.setRoles(updateRole);
        userRepository.save(users);
        return users;
    }

    //TODO Сделать подтверждение кодом по почте перед сбросом пароля
    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest, String username) throws UserNotFoundException{
        Users users = getUserByName(username);
        users.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
        userRepository.save(users);
    }

    public Users getUserByName(String username) throws UserNotFoundException{
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(String.format("User by name: %s not found", username))
        );
    }
}
