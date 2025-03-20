package com.lfey.authservice.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.Date;

@Component
public class JWTUtils {
    private final JwtConfiguration jwtConfiguration;
    private final KeyPair keyPair;

    public JWTUtils(JwtConfiguration jwtConfiguration, KeyPair keyPair) {
        this.jwtConfiguration = jwtConfiguration;
        this.keyPair = keyPair;
    }

    //Generation access token
    public String generationAccessToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtConfiguration.getAccessExpiration()))
                .signWith(keyPair.getPrivate())
                .compact();
    }

    //Generation refresh token
    public String generationRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() +
                        jwtConfiguration.getRefreshExpiration()))
                .signWith(keyPair.getPrivate())
                .compact();
    }

    //Validation token
    public boolean validationToken(String toke) {
        try {
            Jwts.parser()
                    .verifyWith(keyPair.getPublic())
                    .build()
                    .parseSignedClaims(toke);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //Get username from token
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(keyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    //Get expiration date form toke
    public Date extractExpiration(String token) {
        return Jwts.parser()
                .verifyWith(keyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }
}

