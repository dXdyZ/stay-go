package com.staygo.notificationservice.client;

import com.staygo.notificationservice.entity.Users;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/user-service/api/users/by-name/{username}")
    Users getUserByUsername(@PathVariable String username);

    @GetMapping("/user-service/api/users/by-publicId/{publicId}")
    Users getUserByPublicId(@PathVariable UUID publicId);
}
