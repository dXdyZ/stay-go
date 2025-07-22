package com.lfey.authservice.service.verification;

import com.lfey.authservice.dto.ValidationCodeDto;
import com.lfey.authservice.entity.redis.EmailUpdate;
import com.lfey.authservice.entity.redis.UserRegistration;
import com.lfey.authservice.exception.InvalidCodeException;
import com.lfey.authservice.exception.UserCacheDataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationCode {
    private final UserRegService userRegService;
    private final EmailUpdateService emailUpdateService;

    @Autowired
    public VerificationCode(UserRegService userRegService, EmailUpdateService emailUpdateService) {
        this.userRegService = userRegService;
        this.emailUpdateService = emailUpdateService;
    }

    public UserRegistration verificationRegistration(ValidationCodeDto validationCodeDto) throws UserCacheDataNotFoundException,
            InvalidCodeException {

        UserRegistration userRegistration = userRegService.getUserRegByEmail(validationCodeDto.email());
        if (userRegistration.getCode().equals(validationCodeDto.code())) {
            return userRegistration;
        } else throw new InvalidCodeException("Code invalid");
    }

    public EmailUpdate verificationEmailUpdate(ValidationCodeDto validationCodeDto) throws UserCacheDataNotFoundException,
            InvalidCodeException {

        EmailUpdate emailUpdate = emailUpdateService.getEmailUpdateByEmail(validationCodeDto.email());

        if (emailUpdate.getCode().equals(validationCodeDto.code())) {
            return emailUpdate;
        } else throw new InvalidCodeException("Code invalid");
    }
}
