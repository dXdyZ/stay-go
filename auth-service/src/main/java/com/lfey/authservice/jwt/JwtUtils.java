package com.lfey.authservice.jwt;

import com.lfey.authservice.exception.AuthenticationFailedException;
import com.lfey.authservice.exception.InvalidJwtTokenException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.expression.spel.ast.TypeReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
    private final JwtConfiguration jwtConfiguration;
    private final KeyPair keyPair;

    public JwtUtils(JwtConfiguration jwtConfiguration, KeyPair keyPair) {
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
        try {
            return Jwts.parser()
                    .verifyWith(keyPair.getPublic())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtTokenException("Invalid or expired JWT token");
        }
    }

    public List<String> extractRoles(String token) {
        try {
            Object roleObj = Jwts.parser()
                    .verifyWith(keyPair.getPublic())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("roles");

            if (roleObj instanceof List) {
                return ((List<?>) roleObj).stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .toList();
            } else {
                throw new InvalidJwtTokenException("Invalid token");
            }
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtTokenException("Invalid or expired JWT token");
        }
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

