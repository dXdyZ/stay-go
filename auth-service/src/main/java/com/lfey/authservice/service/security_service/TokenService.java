package com.lfey.authservice.service.security_service;

import com.lfey.authservice.dto.JwtTokenDto;
import com.lfey.authservice.dto.UserAuthInfoDto;
import com.lfey.authservice.exception.InvalidJwtTokenException;
import com.lfey.authservice.exception.InvalidRefreshTokenException;
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

    public JwtTokenDto getToken(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        var tokens = new JwtTokenDto(jwtUtils.generationAccessToken(userDetails),
                jwtUtils.generationRefreshToken(userDetails));
        validationRefreshTokenService.addValidToken(tokens.getRefreshToken(), username);
        return tokens;
    }

    /**
     * Протестировать метод на корректность работы
     */
    public JwtTokenDto validationRefreshTokenAndGenerationAccessToken(String token) throws InvalidRefreshTokenException{
        if (!jwtUtils.validationToken(token))
            throw new InvalidRefreshTokenException("Refresh token is invalid or malformed");
        if (!validationRefreshTokenService.isValidToken(token))
            throw new InvalidRefreshTokenException("Refresh token is expired or revoked");

        return new JwtTokenDto(jwtUtils.generationAccessToken(
                userDetailsService.loadUserByUsername(jwtUtils.extractUsername(token))),
                null);
    }

    public UserAuthInfoDto getUserAuthInfo(String token) throws InvalidJwtTokenException {
        return new UserAuthInfoDto(jwtUtils.extractUsername(token), jwtUtils.extractRoles(token));
    }

    public void deleteRefreshToken(String refreshToken) {
        validationRefreshTokenService.deleteToke(refreshToken);
    }
}
