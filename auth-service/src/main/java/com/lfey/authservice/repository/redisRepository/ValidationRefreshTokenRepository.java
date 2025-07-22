package com.lfey.authservice.repository.redisRepository;

import com.lfey.authservice.entity.redis.ValidationRefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidationRefreshTokenRepository extends CrudRepository<ValidationRefreshToken, String> {
    boolean existsByToken(String token);
}
