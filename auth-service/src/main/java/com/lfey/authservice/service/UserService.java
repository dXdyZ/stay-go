package com.lfey.authservice.service;

import com.lfey.authservice.dto.JwtToken;
import com.lfey.authservice.dto.UserReg;
import com.lfey.authservice.dto.ValidationCode;
import com.lfey.authservice.entity.Users;
import com.lfey.authservice.exception.InvalidCodeException;
import com.lfey.authservice.exception.UserRegNotFoundException;
import com.lfey.authservice.repository.jpa.UserRepository;
import com.lfey.authservice.service.verification.GenerationCode;
import com.lfey.authservice.service.verification.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GenerationCode generationCode;
    private final VerificationCode verificationCode;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       GenerationCode generationCode, VerificationCode verificationCode) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.generationCode = generationCode;
        this.verificationCode = verificationCode;
    }

    public void registerUser(UserReg userReg) {
        userReg.setPassword(passwordEncoder.encode(userReg.getPassword()));
        generationCode.generateCode(userReg);
    }

    public JwtToken saveUser(ValidationCode validationCode) throws UserRegNotFoundException, InvalidCodeException {
        UserReg userReg = verificationCode.verification(validationCode);
        Users users = Users.builder()
                .username(userReg.getUsername())
                .password(userReg.getPassword())
                .build();
        userRepository.save(users);
        //TODO сделать пересылку данных через rest
        //TODO Сделать генерацию и возврат токена пользователю
        return null;
    }
}
