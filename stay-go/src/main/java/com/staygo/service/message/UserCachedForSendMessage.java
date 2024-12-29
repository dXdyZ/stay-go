package com.staygo.service.message;

import com.staygo.enity.user.Users;
import com.staygo.service.user_ser.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class UserCachedForSendMessage {
    private final UserService userService;

    @Autowired
    public UserCachedForSendMessage(UserService userService) {
        this.userService = userService;
    }

    @Cacheable(value = "getUserForSendMessage", key = "#id" + '-' + "#username" + '-' + "#password")
    public Users getUserByPrincipal(Principal principal) {
        return userService.findByPrincipal(principal).get();
    }
}
