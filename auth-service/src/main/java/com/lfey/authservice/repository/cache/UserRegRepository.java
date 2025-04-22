package com.lfey.authservice.repository.cache;

import com.lfey.authservice.entity.UserReg;
import org.springframework.data.repository.CrudRepository;

public interface UserRegRepository extends CrudRepository<UserReg, String> {
    Boolean existsByEmail(String email);
}
