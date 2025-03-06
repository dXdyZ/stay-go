package com.lfey.authservice.controller;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.entity.Users;
import com.lfey.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final static String USERNAME_HEADER = "X-User-Username";

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody UserReg userReg) {
        userService.registerUser(userReg);
    }

    @PostMapping("/confirm-registration")
    public JwtToken validationUser(@RequestBody ValidationCode validationCode) {
        return userService.getJWT(validationCode);
    }

    @PostMapping("/update-email")
    public void updateEmail(@RequestBody EmailUpdate emailUpdate,
                            @RequestHeader(USERNAME_HEADER) String username) {
        userService.updateEmail(emailUpdate, username);
    }

    @PostMapping("/confirm-email-update")
    public UserDto validationUpdateEmail(@RequestBody ValidationCode validationCode,
                                         @RequestHeader(USERNAME_HEADER) String username) {
        return userService.updateEmailInUserService(validationCode, username);
    }

    @GetMapping("/{username}")
    public Users getUserByUsername(@PathVariable String username) {
        return userService.getUserByName(username);
    }

    @PatchMapping("/update-username")
    public UserDto updateUsername(@RequestBody UsernameUpdate usernameUpdate,
                                  @RequestHeader(USERNAME_HEADER) String username) {
        return userService.updateUsername(username, usernameUpdate);
    }

    @PatchMapping("/{username}/roles")
    public void addRole(@PathVariable String username,
                        @RequestBody AddRoleRequest addRoleRequest) {
        userService.addRole(username, addRoleRequest);
    }

    @DeleteMapping("/{username}/roles/{role}")
    public Users deleteRole(@PathVariable String role, @PathVariable String username) {
        return userService.deleteRoleUser(username, role);
    }
}
