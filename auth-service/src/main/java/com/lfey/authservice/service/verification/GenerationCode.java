package com.lfey.authservice.service.verification;

import com.lfey.authservice.dto.UserReg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GenerationCode {
    private final UserRegService userRegService;

    @Autowired
    public GenerationCode(UserRegService userRegService) {
        this.userRegService = userRegService;
    }

    public void generateCode(UserReg userReg) {
        userReg.setCode(String.format("%06d", new Random().nextInt(999999)));
        if (!userRegService.existsByEmail(userReg.getEmail())) {
            userRegService.saveUserData(userReg);
            //TODO сделать отправку сообщения в Rabbit
        } else {
            userRegService.removeUserRegByEmail(userReg.getEmail());
            userRegService.saveUserData(userReg);
            //TODO сделать отправку сообщения в Rabbit
        }
    }
}
