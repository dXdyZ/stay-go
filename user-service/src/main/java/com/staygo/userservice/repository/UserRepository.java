package com.staygo.userservice.repository;

import com.staygo.userservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    @Query("select u from Users u where u.publicId = :publicId")
    Optional<Users> findByPublicId(@Param("publicId") UUID publicId);

    Optional<Users> findByPhoneNumber(String phoneNumber);

    Optional<Users> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}