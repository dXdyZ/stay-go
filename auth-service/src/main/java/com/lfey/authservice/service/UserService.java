package com.lfey.authservice.service;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.entity.Users;
import com.lfey.authservice.exception.DuplicateUserException;
import com.lfey.authservice.exception.InvalidCodeException;
import com.lfey.authservice.exception.UserCacheDataNotFoundException;
import com.lfey.authservice.repository.jpa.UserRepository;
import com.lfey.authservice.service.clients.UserClientService;
import com.lfey.authservice.service.verification.GenerationCode;
import com.lfey.authservice.service.verification.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void registerUser(UserReg userReg) throws DuplicateUserException{
        if (!userRepository.existsByUsername(userReg.getUsername())) {
            if (userClientService.getUserByEmailFromUserService(userReg.getEmail()) == null) {
                userReg.setPassword(passwordEncoder.encode(userReg.getPassword()));
                generationCode.generateCode(userReg);
            } else throw new DuplicateUserException(String.format("User with email: %s already exists", userReg.getEmail()));
        } else throw new DuplicateUserException(String.format("The user named: %s already exists", userReg.getUsername()));
    }

    @Transactional
    public JwtToken saveUser(ValidationCode validationCode) throws UserCacheDataNotFoundException, InvalidCodeException {
        UserReg userReg = verificationCode.verification(validationCode);
        Users users = Users.builder()
                .username(userReg.getUsername())
                .password(userReg.getPassword())
                .build();
        userRepository.save(users);

        userClientService.userRegistrationInUserService(UserDto.builder()
                        .email(userReg.getEmail())
                        .phoneNumber(userReg.getPhoneNumber())
                        .username(userReg.getUsername())
                .build());

        //TODO Сделать генерацию и возврат токена пользователю
        return null;
    }

    //TODO Сделать обновление почты после подтверждения кодом 
    public void updateEmail(EmailUpdate emailUpdate) {
        UserDto userDto = userClientService.getUserByEmailFromUserService(emailUpdate.email());

    }

}
