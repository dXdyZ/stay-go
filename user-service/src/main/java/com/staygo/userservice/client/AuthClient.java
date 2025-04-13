package com.staygo.userservice.client;

import com.staygo.userservice.config.FeignClintConfiguration;
import com.staygo.userservice.dto.RoleRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", configuration = FeignClintConfiguration.class)
public interface AuthClient {


    @PatchMapping("/api/users/{username}/roles")
    void addRole(@PathVariable String username,
                 @RequestBody RoleRequest roleRequest);
}
