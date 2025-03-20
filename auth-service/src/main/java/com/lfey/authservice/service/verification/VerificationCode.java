package com.lfey.authservice.service.verification;

import com.lfey.authservice.entity.UserReg;
import com.lfey.authservice.dto.ValidationCode;
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

    public UserReg verification(ValidationCode validationCode) throws UserCacheDataNotFoundException, InvalidCodeException {
        UserReg userReg = userRegService.getUserRegByEmail(validationCode.email());
        if (userReg.getCode().equals(validationCode.code())) {
            return userReg;
        } else throw new InvalidCodeException("Invalid code");
    }
}
