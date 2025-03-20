package com.lfey.authservice.service;

import com.lfey.authservice.dto.JwtToken;
import com.lfey.authservice.jwt.JWTUtils;
import com.lfey.authservice.service.security_service.ValidationRefreshTokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final UserDetailsService userDetailsService;
    private final JWTUtils jwtUtils;
    private final ValidationRefreshTokenService validationRefreshTokenService;

    public TokenService(UserDetailsService userDetailsService, JWTUtils jwtUtils,
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

    public void deleteRefreshToken(String refreshToken) {
        validationRefreshTokenService.deleteToke(refreshToken);
    }
}
