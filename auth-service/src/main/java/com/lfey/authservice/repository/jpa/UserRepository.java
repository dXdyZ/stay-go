package com.lfey.authservice.repository.jpa;

import com.lfey.authservice.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Users, Long> {
    Boolean existsByUsername(String username);

    Optional<Users> findByUsername(String username);
}
