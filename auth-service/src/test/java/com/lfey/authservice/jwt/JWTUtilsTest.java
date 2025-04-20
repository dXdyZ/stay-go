package com.lfey.authservice.jwt;

import static org.junit.jupiter.api.Assertions.*;

import com.lfey.authservice.entity.Role;
import com.lfey.authservice.entity.RoleName;
import com.lfey.authservice.entity.SecurityUser;
import com.lfey.authservice.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class JWTUtilsTest {

    private static final Logger log = LoggerFactory.getLogger(JWTUtilsTest.class);
    private JwtUtils jwtUtils;
    private KeyPair keyPair;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        // 1. Генерация RSA-ключей в памяти
        keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

        // 2. Тестовая конфигурация
        JwtConfiguration testConfig = new JwtConfiguration();
        testConfig.setAccessExpiration(900_000L); // 15 минут
        testConfig.setRefreshExpiration(6_048_000L); // 7 дней

        // 3. Инициализация тестируемого класса
        jwtUtils = new JwtUtils(testConfig, keyPair);

        // 4. Тестовый пользователь
        userDetails = new SecurityUser(
                Users.builder()
                        .username("testUser")
                        .password("password")
                        .roles(Set.of(Role.builder()
                                        .roleName(RoleName.ROLE_USER)
                                .build()))
                        .build()
        );
    }

    @Test
    void generationAccessToken_ValidUser_ReturnsValidToken() {
        // when
        String token = jwtUtils.generationAccessToken(userDetails);

        // then
        assertNotNull(token);
        log.info("Generated Token: {}", token);

        // Проверка структуры токена (Header.Payload.Signature)
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length);

        // Проверка подписи с публичным ключом
        Jws<Claims> jws = Jwts.parser()
                .verifyWith(keyPair.getPublic())
                .build()
                .parseSignedClaims(token);

        // Проверка claims
        assertEquals("testUser", jws.getPayload().getSubject());
        assertTrue(jws.getPayload().getExpiration().after(new Date()));
    }
}