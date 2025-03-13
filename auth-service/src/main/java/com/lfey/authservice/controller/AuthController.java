package com.lfey.authservice.controller;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.entity.Users;
import com.lfey.authservice.service.UserService;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {
    public final static String USERNAME_HEADER = "X-User-Username";

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
    public ResponseEntity<JwtToken> validationUser(@RequestBody ValidationCode validationCode) {
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build().toUri()).body(userService.getJWT(validationCode));
    }

    @PostMapping("/update-email")
    public void updateEmail(@RequestBody EmailUpdate emailUpdate,
                            @RequestHeader(USERNAME_HEADER) String username) {
        userService.updateEmail(emailUpdate, username);
    }

    @PostMapping("/confirm-email-update")
    public ResponseEntity<UserDto> validationUpdateEmail(@RequestBody ValidationCode validationCode,
                                         @RequestHeader(USERNAME_HEADER) String username) {
        return ResponseEntity.ok(userService.updateEmailInUserService(validationCode, username));
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

    @PatchMapping("/{username}/roles")
    public void addRole(@PathVariable String username,
                        @RequestBody AddRoleRequest addRoleRequest) {
        userService.addRole(username, addRoleRequest);
    }

    @DeleteMapping("/{username}/roles/{role}")
    public void deleteRole(@PathVariable String role, @PathVariable String username) {
        userService.deleteRoleUser(username, role);
    }
}
