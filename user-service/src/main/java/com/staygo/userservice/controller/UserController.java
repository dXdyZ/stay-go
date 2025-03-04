package com.staygo.userservice.controller;

import com.staygo.userservice.dto.UserDto;
import com.staygo.userservice.entity.Users;
import com.staygo.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final String USERNAME_HEADER = "X-User-Username";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void saveUser(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
    }

    @GetMapping("/{id}")
    public Users getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/by-name/{username}")
    public Users getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/by-email/{email}")
    public Users getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/by-phone/{phone}")
    public Users getUserByPhone(@PathVariable String phone) {
        return userService.getUserByPhoneNumber(phone);
    }

    @PatchMapping("/update-username/{newUsername}")
    public Users updateUsername(@PathVariable String newUsername,
                               @RequestHeader(USERNAME_HEADER) String username) {
        return userService.updateUsername(username, newUsername);
    }

    @PatchMapping("/update-phone/{phone}")
    public Users updatePhone(@PathVariable String phone,
                            @RequestHeader(USERNAME_HEADER) String username) {
        return userService.updatePhoneNumber(username, phone);
    }

    @PatchMapping("/update-email/{email}")
    public Users updateEmail(@PathVariable String email,
                             @RequestHeader(USERNAME_HEADER) String username) {
        return userService.updateEmail(email, username);
    }
}
