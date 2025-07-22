package com.lfey.authservice.jwt;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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


    @Value("${jwt.keys.path}")
    private String keysPath;

    @PostConstruct
    public void checkKeys() {
        Path privateKeyPath = Paths.get(keysPath, "private-key.txt");
        Path publicKeyPath = Paths.get(keysPath, "public-key.txt");

        if (!Files.exists(privateKeyPath) || !Files.isReadable(privateKeyPath) ||
                !Files.exists(publicKeyPath) || !Files.isReadable(publicKeyPath)) {
            throw new IllegalStateException(
                    "CRITICAL ERROR: JWT keys are missing or not readable.\n" +
                            "Private key path: " + privateKeyPath.toAbsolutePath() + "\n" +
                            "Public key path: " + publicKeyPath.toAbsolutePath()
            );
        }
    }

    @Bean
    public KeyPair keyPair() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        Path privateKeyPath = Paths.get(keysPath, "private-key.txt");
        Path publicKeyPath = Paths.get(keysPath, "public-key.txt");

        String privateKeyPEM = new String(Files.readAllBytes(privateKeyPath)).trim();
        String publicKeyPEM = new String(Files.readAllBytes(publicKeyPath)).trim();

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        return new KeyPair(
                kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes)),
                kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes))
        );
    }
}
