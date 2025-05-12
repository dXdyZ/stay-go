package com.lfey.authservice.controller;

import com.lfey.authservice.controller.documentation.UserControllerDocs;
import com.lfey.authservice.dto.*;
import com.lfey.authservice.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/users/")
@Tag(name = "User authentication API", description = "Management user data for authentication")
public class UserController implements UserControllerDocs {
    private final UserService userService;

    public final static String USERNAME_HEADER = "X-User-Username";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserBriefDto> getBriefUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getBriefUserByUsername(username));
    }

    @PatchMapping("/username")
    public ResponseEntity<UserDetailsDto> updateUsername(@Valid  @RequestBody UsernameUpdateDto usernameUpdateDto,
                                                         @RequestHeader(USERNAME_HEADER) String username) {
        return ResponseEntity.ok(userService.updateUsername(username, usernameUpdateDto));
    }

    @PatchMapping("/email")
    public void updateEmail(@Valid @RequestBody EmailUpdateDto emailUpdateDto,
                            @RequestHeader(USERNAME_HEADER) String username) {
        userService.updateEmail(emailUpdateDto, username);
    }

    @PatchMapping("/password")
    public void updatePassword(@Valid @RequestBody ResetPasswordRequestDto passwordRequest,
                               @RequestHeader(USERNAME_HEADER) String username) {
        userService.resetPassword(passwordRequest, username);
    }

    @PatchMapping("/{username}/roles")
    public void addRole(@PathVariable String username,
                        @RequestBody RoleRequestDto addRoleRequestDto) {
        userService.addRole(username, addRoleRequestDto);
    }

    @DeleteMapping("/{username}/roles")
    public void deleteRole(@PathVariable String username,
                           @RequestBody RoleRequestDto roleRequestDto) {
        userService.deleteRoleUser(username, roleRequestDto);
    }
}
