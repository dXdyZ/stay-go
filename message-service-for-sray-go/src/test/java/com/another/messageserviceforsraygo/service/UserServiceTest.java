package com.another.messageserviceforsraygo.service;

import com.another.messageserviceforsraygo.entity.User;
import com.another.messageserviceforsraygo.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);
    @Autowired
    private UserService userService;



    @Test
    @Rollback(false)
    void saveUser() {
        User user = User.builder()
                .name("hello")
                .id("1")
                .build();
        userService.saveUser(user);
    }

    @Test
    @Rollback(false)
    void addedFriend() {
        userService.friendAdd("3", "hello");
    }

    @Test
    void testSaveGroupUser() {
        List<User> users = new ArrayList<>() {{
            add(new User("3", "hello", null));
            add(new User("10", "testSave", null));
        }};
        log.info("users data list: {}", users);
    }

    @Test
    void testMethodFindAllById() {
        log.info("test method find all by id: {}", userService.getUserByListId(List.of("3", "10")));
    }

    @Test
    void getUserById() {
        log.info("get user by id: {}", userService.getByName("hello"));
    }

    @Test
    void getAllUsers() {
        log.info("user all user: {}", userService.getAllUsers());
    }
}