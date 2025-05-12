package com.lfey.authservice.service;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.dto.kafka.EventType;
import com.lfey.authservice.entity.Role;
import com.lfey.authservice.entity.RoleName;
import com.lfey.authservice.entity.UserRegistration;
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
    public void updateEmail(EmailUpdateDto emailUpdateDto, String username) throws DuplicateUserException {
        //Может стоит обновить проверку на null через метод Objects.requireNonNull()
        UserDetailsDto userDetailsDto = userClientService.getUserByEmailFromUserService(emailUpdateDto.email());
        if (userDetailsDto == null) {
            generationCode.generateCode(UserRegistration.builder()
                    .email(emailUpdateDto.email())
                    .username(username)
                    .build(), EventType.EMAIL_RESET);
        } else
            throw new DuplicateUserException(String.format("User with email: %s already exists", emailUpdateDto.email()));
    }

    public UserDetailsDto updateEmailInUserService(ValidationCodeDto validationCodeDto, String username) throws ServerErrorException {
        UserRegistration userRegistration = verificationCode.verification(validationCodeDto);
        return userClientService.updateUserEmailInUserService(userRegistration.getEmail(), username);
    }

    @Transactional
    public UserDetailsDto updateUsername(String username, UsernameUpdateDto usernameUpdateDto) throws ServerErrorException,
            DuplicateUserException{
        if (userRepository.findByUsername(usernameUpdateDto.newUsername()).orElse(null) == null) {
            UserDetailsDto userDetailsDto = userClientService.updateUsernameInUserService(usernameUpdateDto.newUsername(), username);
            Users users = userRepository.findByUsername(username).get();
            users.setUsername(userDetailsDto.getUsername());
            userRepository.save(users);
            return userDetailsDto;
        } else throw new DuplicateUserException(
                String.format("User with username: %s already exists", usernameUpdateDto.newUsername()));
    }

    @Transactional
    public void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto, String username) throws UserNotFoundException {
        Users users = getUserByName(username);

        UserDetailsDto userFromUserService = userClientService.getUserByUsernameFromUserService(users.getUsername());
        generationCode.generateCode(UserRegistration.builder()
                        .email(userFromUserService.getEmail())
                        .password(passwordEncoder.encode(resetPasswordRequestDto.newPassword()))
                .build(), EventType.PASSWORD_RESET);
    }

    @Transactional
    public void addRole(String username, RoleRequestDto addRoleRequestDto) throws UserNotFoundException, DuplicateRoleException{
        Users users = getUserByName(username);
        boolean status = users.getRoles().add(Role.builder().roleName(addRoleRequestDto.role()).build());
        if (status) {
            userRepository.save(users);
        } else {
            throw new DuplicateRoleException(
                    String.format("Role: %s is already assigned to the user", addRoleRequestDto.role()));
        }
    }

    @Transactional
    public void deleteRoleUser(String username, RoleRequestDto role) throws UserNotFoundException{
        Users users = getUserByName(username);
        Set<Role> updateRole = users.getRoles().stream()
                .filter(roleUpdate -> !roleUpdate.getRoleName().equals(role.role()))
                .collect(Collectors.toSet());
        users.setRoles(updateRole);
        userRepository.save(users);
    }

    public UserBriefDto getBriefUserByUsername(String username) throws UserNotFoundException{
        Users users = getUserByName(username);
        return UserBriefDto.builder()
                .username(users.getUsername())
                .roles(users.getRoles().stream()
                        .map(Role::getRoleName)
                        .toList())
                .build();
    }

    public Users getUserByName(String username) throws UserNotFoundException{
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(String.format("User by name: %s not found", username))
        );
    }
}
