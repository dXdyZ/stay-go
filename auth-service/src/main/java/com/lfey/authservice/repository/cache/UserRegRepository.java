package com.lfey.authservice.repository.cache;

import com.lfey.authservice.entity.UserRegistration;
import org.springframework.data.repository.CrudRepository;

public interface UserRegRepository extends CrudRepository<UserRegistration, String> {
    Boolean existsByEmail(String email);
}
