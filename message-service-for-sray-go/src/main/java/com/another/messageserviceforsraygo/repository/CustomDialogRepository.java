package com.another.messageserviceforsraygo.repository;

import com.another.messageserviceforsraygo.entity.Dialog;
import com.another.messageserviceforsraygo.entity.Message;
import com.another.messageserviceforsraygo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomDialogRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomDialogRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void addedMessageInDialog(List<User> users, Message message) {
        List<String> userId = users.stream()
                .map(User::getId)
                .toList();
        Query query = new Query(Criteria.where("users._id").all(userId));
        Update update = new Update().push("message", message);
        mongoTemplate.updateFirst(query, update, Dialog.class);
    }
}
