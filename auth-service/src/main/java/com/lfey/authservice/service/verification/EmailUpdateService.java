package com.lfey.authservice.service.verification;

import com.lfey.authservice.entity.redis.EmailUpdate;
import com.lfey.authservice.exception.UserCacheDataNotFoundException;
import com.lfey.authservice.repository.redisRepository.EmailUpdateRepository;
import org.springframework.stereotype.Service;

@Service
public class EmailUpdateService {
    private final EmailUpdateRepository emailUpdateRepository;

    public EmailUpdateService(EmailUpdateRepository emailUpdateRepository) {
        this.emailUpdateRepository = emailUpdateRepository;
    }

    public Boolean existsByEmail(String email) {
        return emailUpdateRepository.existsByNewEmail(email);
    }

    public void saveEmailUpdateData(EmailUpdate emailUpdate) {
        emailUpdateRepository.save(emailUpdate);
    }

    public void removeEmailUpdateDataByEmail(String email) {
        emailUpdateRepository.deleteById(email);
    }

    public EmailUpdate getEmailUpdateByEmail(String email) {
        return emailUpdateRepository.findById(email).orElseThrow(
                () -> new UserCacheDataNotFoundException("Code invalid")
        );
    }
}
