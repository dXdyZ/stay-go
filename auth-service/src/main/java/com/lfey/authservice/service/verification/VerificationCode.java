package com.lfey.authservice.service.verification;

import com.lfey.authservice.dto.ValidationCodeDto;
import com.lfey.authservice.entity.UserRegistration;
import com.lfey.authservice.exception.InvalidCodeException;
import com.lfey.authservice.exception.UserCacheDataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationCode {
    private final UserRegService userRegService;

    @Autowired
    public VerificationCode(UserRegService userRegService) {
        this.userRegService = userRegService;
    }

    public UserRegistration verification(ValidationCodeDto validationCodeDto) throws UserCacheDataNotFoundException, InvalidCodeException {
        UserRegistration userRegistration = userRegService.getUserRegByEmail(validationCodeDto.email());
        if (userRegistration.getCode().equals(validationCodeDto.code())) {
            return userRegistration;
        } else throw new InvalidCodeException("Invalid code");
    }
}
