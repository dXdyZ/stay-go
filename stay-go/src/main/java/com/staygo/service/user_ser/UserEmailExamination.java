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

    private final Map<Integer, Users> userCode = new HashMap<>();


    public void sendGenerationCodeOnUserEmail(Users users) {
        Integer code = generationRandomNumberForCode();
        userCode.put(code, users);
        mailService.sendMail(users.getEmail(), "Verification code", code.toString());
    }

    public Users examinationCode(String email, Integer code) {
        if (!userCode.isEmpty() && userCode.get(code) != null) {
            Users codeUser = userCode.get(code);
            if (codeUser != null && userCode.get(code).getEmail().equals(email)) {
                ScheduledFuture<?> scheduledFuture = scheduler.schedule(() -> {
                    clearMapWithUsersCode(code);
                }, 10, TimeUnit.MINUTES);
                return userCode.get(code);
            }
        }
        return null;
    }

    private void clearMapWithUsersCode(Integer code) {
        userCode.remove(code);
    }

    private int generationRandomNumberForCode() {
        int max = 99999, min = 1111;
        return (int) (Math.random() * ++max) + min;
    }
}
