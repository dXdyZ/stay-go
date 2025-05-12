package com.lfey.authservice.repository.cache;

import com.lfey.authservice.entity.ValidationRefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidationRefreshTokenRepository extends CrudRepository<ValidationRefreshToken, String> {
    boolean existsByToken(String token);
}
