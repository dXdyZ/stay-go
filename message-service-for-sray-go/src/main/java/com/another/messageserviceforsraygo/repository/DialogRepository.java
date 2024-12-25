package com.another.messageserviceforsraygo.repository;

import com.another.messageserviceforsraygo.entity.Dialog;
import com.another.messageserviceforsraygo.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DialogRepository extends MongoRepository<Dialog, String> {
    Page<Dialog> findByUsers(List<User> users, Pageable pageable);
}
