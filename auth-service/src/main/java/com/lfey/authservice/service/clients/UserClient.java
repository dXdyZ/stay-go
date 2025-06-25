package com.lfey.authservice.service.clients;


import com.lfey.authservice.configuration.FeignClintConfiguration;
import com.lfey.authservice.dto.UserDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", configuration = FeignClintConfiguration.class)
public interface UserClient {
    @PostMapping("/user-service/api/users/save")
    void saveUser(@RequestBody UserDetailsDto userDetailsDto);

    @GetMapping("/user-service/api/users/by-email/{email}")
    UserDetailsDto getUserByEmail(@PathVariable String email);

    @GetMapping("/user-service/api/users/by-name/{username}")
    UserDetailsDto getUserByUsername(@PathVariable String username);

    @PatchMapping("/user-service/api/users/{email}/email")
    UserDetailsDto updateEmail(@PathVariable String email,
                               @RequestHeader("X-User-Username") String username);

    @PatchMapping("/user-service/api/users/{newUsername}/username")
    UserDetailsDto updateUsername(@PathVariable String newUsername,
                                  @RequestHeader("X-User-Username") String username);
}
