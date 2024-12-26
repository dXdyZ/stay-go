package com.another.messageserviceforsraygo.controller;

import com.another.messageserviceforsraygo.entity.User;
import com.another.messageserviceforsraygo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message-user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") String id) {
        return userService.getUserId(id);
    }

    @PostMapping("/added")
    public void addedUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @GetMapping("/all")
    public List<User> getAllUser() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/delete-friend/{id}/{name}")
    public void deleteFriend(@PathVariable("id") String id, @PathVariable("name") String name) {
        userService.deletingFriend(id, name);
    }
}
