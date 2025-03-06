package com.lfey.authservice.service.clients;


import com.lfey.authservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserClient {
    @PostMapping("/users/register")
    void saveUser(@RequestBody UserDto userDto);

    @GetMapping("/users/by-email/{email}")
    UserDto getUserByEmail(@PathVariable String email);

    @PatchMapping("/users/update-email/{email}")
    UserDto updateEmail(@PathVariable String email,
                        @RequestHeader("X-User-Username") String username);

    @PatchMapping("/users/update-username/{newUsername}")
    UserDto updateUsername(@PathVariable String newUsername,
                           @RequestHeader("X-User-Username") String username);
}
