package com.lfey.authservice.repository.cache;

import com.lfey.authservice.dto.UserReg;
import jakarta.validation.constraints.Email;
import org.springframework.data.repository.CrudRepository;

public interface UserRegRepository extends CrudRepository<UserReg, String> {
    Boolean existsByEmail(String email);
}
