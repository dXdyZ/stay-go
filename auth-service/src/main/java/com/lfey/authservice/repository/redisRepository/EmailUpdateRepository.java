package com.lfey.authservice.repository.redisRepository;

import com.lfey.authservice.entity.redis.EmailUpdate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailUpdateRepository extends CrudRepository<EmailUpdate, String> {
    Boolean existsByNewEmail(String newEmail);
}
