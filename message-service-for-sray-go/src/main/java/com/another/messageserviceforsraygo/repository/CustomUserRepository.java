package com.another.messageserviceforsraygo.repository;

import com.another.messageserviceforsraygo.entity.Friend;
import com.another.messageserviceforsraygo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomUserRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomUserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void updateUserFriend(String id, String fieldName, Friend fieldValue) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().push(fieldName).each(fieldValue);
        mongoTemplate.updateFirst(query, update, User.class);
    }

    public void removeFriendByName(String userId, String friendName) {
        Query query = new Query(Criteria.where("id").is(userId));
        Update update = new Update().pull("friends", new Query(Criteria.where("name").is(friendName)).getQueryObject());
        mongoTemplate.updateFirst(query, update, User.class);
    }
}
