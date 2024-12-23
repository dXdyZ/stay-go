package com.another.messageserviceforsraygo.repository;

import com.another.messageserviceforsraygo.entity.Dialog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogRepository extends MongoRepository<Dialog, String> {
}
