package com.lfey.authservice.controller;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.entity.Users;
import com.lfey.authservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public final static String USERNAME_HEADER = "X-User-Username";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<Users> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByName(username));
    }

    @PatchMapping("/update-username")
    public ResponseEntity<UserDto> updateUsername(@RequestBody UsernameUpdate usernameUpdate,
                                                  @RequestHeader(USERNAME_HEADER) String username) {
        return ResponseEntity.ok(userService.updateUsername(username, usernameUpdate));
    }

    @PatchMapping("/update-email")
    public void updateEmail(@RequestBody EmailUpdate emailUpdate,
                            @RequestHeader(USERNAME_HEADER) String username) {
        userService.updateEmail(emailUpdate, username);
    }

    @PatchMapping("/update-password")
    public void updatePassword(@RequestBody ResetPasswordRequest passwordRequest) {
        userService.resetPassword(passwordRequest);
    }

    @PatchMapping("/{username}/roles")
    public void addRole(@PathVariable String username,
                        @RequestBody RoleRequest addRoleRequest) {
        userService.addRole(username, addRoleRequest);
    }

    @DeleteMapping("/{username}/roles")
    public void deleteRole(@PathVariable String username,
                           @RequestBody RoleRequest roleRequest) {
        userService.deleteRoleUser(username, roleRequest);
    }
}
