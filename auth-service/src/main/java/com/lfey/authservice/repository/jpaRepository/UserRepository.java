package com.lfey.authservice.repository.jpaRepository;

import com.lfey.authservice.entity.jpa.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Boolean existsByUsername(String username);

    Optional<Users> findByUsername(String username);

    Optional<Users> findByPublicId(UUID publicId);
}
