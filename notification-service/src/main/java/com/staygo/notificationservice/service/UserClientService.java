package com.staygo.notificationservice.service;

import com.staygo.notificationservice.client.UserClient;
import com.staygo.notificationservice.entity.Users;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserClientService {
    private final UserClient userClient;

    public UserClientService(UserClient userClient) {
        this.userClient = userClient;
    }

    @Cacheable(value = "username", key = "#username")
    public Users getUserByUsername(String username) {
        return userClient.getUserByUsername(username);
    }

}
