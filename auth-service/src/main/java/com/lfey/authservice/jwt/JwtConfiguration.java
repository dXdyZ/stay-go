package com.lfey.authservice.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Data
@Configuration
public class JwtConfiguration {

    @Value("${jwt.access.expiration}")
    private Long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    //TODO Сделать правильную обработку исключений в случае ошибки, разделить метод на методы
    @Bean
    public KeyPair keyPair() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        String privateKeyPEM = new String(
                Files.readAllBytes(new ClassPathResource("private-key.txt").getFile().toPath()))
                .trim();
        String publicKeyPEM = new String(
                Files.readAllBytes(new ClassPathResource("public-key.txt").getFile().toPath()))
                .trim();

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        return new KeyPair(
                kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes)),
                kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes))
        );
    }
}
