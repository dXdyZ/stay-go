package com.lfey.authservice.service;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.dto.kafka.EventType;
import com.lfey.authservice.entity.jpa.Role;
import com.lfey.authservice.entity.redis.EmailUpdate;
import com.lfey.authservice.entity.redis.UserRegistration;
import com.lfey.authservice.entity.jpa.Users;
import com.lfey.authservice.exception.*;
import com.lfey.authservice.repository.jpaRepository.UserRepository;
import com.lfey.authservice.service.clients.UserClientService;
import com.lfey.authservice.service.verification.GenerationCode;
import com.lfey.authservice.service.verification.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
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
    public void updateEmail(EmailUpdateDto emailUpdateDto, UUID publicId) throws DuplicateUserException {
        UserDetailsDto userDetailsDto = userClientService.getUserByEmailFromUserService(emailUpdateDto.email());
        if (userDetailsDto == null) {
            generationCode.generateCode(EmailUpdate.builder()
                            .newEmail(emailUpdateDto.email())
                            .publicId(publicId)
                    .build());
        } else
            throw new DuplicateUserException(String.format("User with email: %s already exists", emailUpdateDto.email()));
    }

    public UserDetailsDto updateEmailInUserService(ValidationCodeDto validationCodeDto, UUID publicId) throws ServerErrorException,
            UserCacheDataNotFoundException{

        EmailUpdate emailUpdate = verificationCode.verificationEmailUpdate(validationCodeDto);
        return userClientService.updateUserEmailInUserService(emailUpdate.getNewEmail(), publicId);
    }

    @Transactional
    public UserDetailsDto updateUsername(UUID publicId, UsernameUpdateDto usernameUpdateDto) throws ServerErrorException,
            DuplicateUserException{
        if (userRepository.existsByUsername(usernameUpdateDto.newUsername())) {
            throw new DuplicateUserException(
                    String.format("User with username: %s already exists", usernameUpdateDto.newUsername()));
        }
        UserDetailsDto userDetailsDto = userClientService.updateUsernameInUserService(usernameUpdateDto.newUsername(), publicId);
        Users users = userRepository.findByPublicId(publicId).get();
        users.setUsername(userDetailsDto.getUsername());
        userRepository.save(users);
        return userDetailsDto;
    }

    @Transactional
    public void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto, UUID publicId) {
        UserDetailsDto userFromUserService = userClientService.getUserByPublicIdFromUserService(publicId);
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

        users.setRoles(users.getRoles().stream()
                .filter(roleUpdate -> !roleUpdate.getRoleName().equals(role.role()))
                .collect(Collectors.toSet()));

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
