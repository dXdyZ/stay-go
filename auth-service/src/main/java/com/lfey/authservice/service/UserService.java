package com.lfey.authservice.service;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.dto.rabbit.EventType;
import com.lfey.authservice.entity.Role;
import com.lfey.authservice.entity.RoleName;
import com.lfey.authservice.entity.UserReg;
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

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final GenerationCode generationCode;
    private final VerificationCode verificationCode;
    private final UserClientService userClientService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, GenerationCode generationCode,
                       VerificationCode verificationCode, UserClientService userClientService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.generationCode = generationCode;
        this.verificationCode = verificationCode;
        this.userClientService = userClientService;
        this.passwordEncoder = passwordEncoder;
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

    public UserDto updateEmailInUserService(ValidationCode validationCode, String username) throws ServerErrorException {
        UserReg userReg = verificationCode.verification(validationCode);
        return userClientService.updateUserEmailInUserService(userReg.getEmail(), username);
    }

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
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) throws UserNotFoundException {
        Users users = userRepository.findByUsername(resetPasswordRequest.username()).orElseThrow(
                () -> new UserNotFoundException(String.format("User by name: %s not found", resetPasswordRequest.username()))
        );
        UserDto userFromUserService = userClientService.getUserByUsernameFromUserService(users.getUsername());
        generationCode.generateCode(UserReg.builder()
                        .email(userFromUserService.getEmail())
                        .password(passwordEncoder.encode(resetPasswordRequest.newPassword()))
                .build(), EventType.PASSWORD_RESET);
    }

    @Transactional
    public void addRole(String username, RoleRequest addRoleRequest) throws UserNotFoundException, DuplicateRoleException{
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
    public void deleteRoleUser(String username, RoleRequest role) throws UserNotFoundException{
        Users users = getUserByName(username);
        Set<Role> updateRole = users.getRoles().stream()
                .filter(roleUpdate -> !roleUpdate.getRoleName().equals(RoleName.valueOf(role.role())))
                .collect(Collectors.toSet());
        users.setRoles(updateRole);
        userRepository.save(users);
    }

    public Users getUserByName(String username) throws UserNotFoundException{
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(String.format("User by name: %s not found", username))
        );
    }
}
