package com.another.messageserviceforsraygo.service;

import com.another.messageserviceforsraygo.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

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
    void getAllUsers() {
        log.info("user all user: {}", userService.getAllUsers());
    }
}