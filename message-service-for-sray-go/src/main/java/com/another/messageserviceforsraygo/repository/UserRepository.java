package com.another.messageserviceforsraygo.repository;

import com.another.messageserviceforsraygo.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByName(String name);
    List<User> findByIdIn(List<String> ids);
}
