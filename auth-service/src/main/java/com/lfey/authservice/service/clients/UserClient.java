package com.lfey.authservice.service.clients;


import com.lfey.authservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserClient {
    @PostMapping("/users/register")
    void saveUser(@RequestBody UserDto userDto);

    @GetMapping("/users/by-email/{email}")
    UserDto getUserByEmail(@PathVariable String email);
}
