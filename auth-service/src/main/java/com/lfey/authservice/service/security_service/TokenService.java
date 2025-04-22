package com.lfey.authservice.service.security_service;

import com.lfey.authservice.dto.JwtToken;
import com.lfey.authservice.jwt.JwtUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final ValidationRefreshTokenService validationRefreshTokenService;

    public TokenService(UserDetailsService userDetailsService, JwtUtils jwtUtils,
                        ValidationRefreshTokenService validationRefreshTokenService) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.validationRefreshTokenService = validationRefreshTokenService;
    }

    public JwtToken getToken(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        var tokens = new JwtToken(jwtUtils.generationAccessToken(userDetails),
                jwtUtils.generationRefreshToken(userDetails));
        validationRefreshTokenService.addValidToken(tokens.getRefreshToken(), username);
        return tokens;
    }

    /**
     * Протестировать метод на корректность работы
     */
    public JwtToken validationRefreshTokenAndGenerationAccessToken(String token) {
        if (jwtUtils.validationToken(token) && validationRefreshTokenService.isValidToken(token)) {
            return new JwtToken(jwtUtils.generationAccessToken(
                    userDetailsService.loadUserByUsername(jwtUtils.extractUsername(token))), null);
        }
        //TODO заменить на исключение
        return null;
    }

    public void deleteRefreshToken(String refreshToken) {
        validationRefreshTokenService.deleteToke(refreshToken);
    }
}
