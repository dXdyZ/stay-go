package com.staygo.userservice.repository;

import com.staygo.userservice.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    Optional<Users> findByPhoneNumber(String phoneNumber);

    Optional<Users> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}