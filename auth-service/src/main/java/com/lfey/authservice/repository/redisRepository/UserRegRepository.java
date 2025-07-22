package com.lfey.authservice.repository.redisRepository;

import com.lfey.authservice.entity.redis.UserRegistration;
import org.springframework.data.repository.CrudRepository;

public interface UserRegRepository extends CrudRepository<UserRegistration, String> {
    Boolean existsByEmail(String email);
}
