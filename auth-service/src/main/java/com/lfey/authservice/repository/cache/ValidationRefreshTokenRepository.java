package com.lfey.authservice.repository.cache;

import com.lfey.authservice.entity.ValidationRefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidationRefreshTokenRepository extends CrudRepository<ValidationRefreshToken, String> {
    void deleteByUsername(String username);
    boolean existsByToken(String token);
}
