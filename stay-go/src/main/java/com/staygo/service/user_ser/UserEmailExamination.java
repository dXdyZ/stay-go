package com.staygo.service.user_ser;

import com.staygo.enity.user.Users;
import com.staygo.service.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserEmailExamination {
    private final MailService mailService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    public UserEmailExamination(MailService mailService) {
        this.mailService = mailService;
    }

    private final Map<Users, Integer> userCode = new HashMap<>();


    public void sendGenerationCodeOnUserEmail(Users users) {
        Integer code = generationRandomNumberForCode();
        userCode.put(users, code);
        mailService.sendMail(users.getEmail(), "Verification code", code.toString());
    }

    public Users examinationCode(Users users, Integer code) {
        if (!userCode.isEmpty() && userCode.get(users) != null) {
            Integer codeUser = userCode.get(users);
            if (codeUser != null && code.equals(userCode.get(users))) {
                ScheduledFuture<?> scheduledFuture = scheduler.schedule(() -> {
                    clearMapWithUsersCode(users);
                }, 10, TimeUnit.MINUTES);
                return users;
            }
        }
        return null;
    }

    private void clearMapWithUsersCode(Users users) {
        userCode.remove(users);
    }

    private int generationRandomNumberForCode() {
        int max = 11111, min = 99999;
        return (int) (Math.random() * ++max) + min;
    }
}
