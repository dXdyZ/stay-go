package com.lfey.authservice.service.security_service;

import com.lfey.authservice.entity.redis.ValidationRefreshToken;
import com.lfey.authservice.repository.redisRepository.ValidationRefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class ValidationRefreshTokenService {
    private final ValidationRefreshTokenRepository refreshTokenRepository;

    public ValidationRefreshTokenService(ValidationRefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void addValidToken(String token, String username) {
        refreshTokenRepository.save(ValidationRefreshToken.builder()
                        .token(token)
                        .username(username)
                .build());
    }

    public boolean isValidToken(String token) {
        return refreshTokenRepository.existsByToken(token);
    }

    public void deleteToke(String token) {
        refreshTokenRepository.deleteById(token);
    }
}






