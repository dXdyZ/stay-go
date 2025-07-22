package com.lfey.authservice.controller;

import com.lfey.authservice.controller.documentation.UserControllerDocs;
import com.lfey.authservice.dto.*;
import com.lfey.authservice.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth/users/")
@Tag(name = "User authentication API", description = "Management user data for authentication")
public class UserController implements UserControllerDocs {
    private final UserService userService;

    public final static String USER_PUBLIC_ID = "X-User-PublicId";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserBriefDto> getBriefUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getBriefUserByUsername(username));
    }

    @PatchMapping("/username")
    public ResponseEntity<UserDetailsDto> updateUsername(@Valid  @RequestBody UsernameUpdateDto usernameUpdateDto,
                                                         @RequestHeader(USER_PUBLIC_ID) UUID publicId) {
        return ResponseEntity.ok(userService.updateUsername(publicId, usernameUpdateDto));
    }

    @PatchMapping("/email")
    public void updateEmail(@Valid @RequestBody EmailUpdateDto emailUpdateDto,
                            @RequestHeader(USER_PUBLIC_ID) UUID publicId) {
        userService.updateEmail(emailUpdateDto, publicId);
    }

    @PatchMapping("/password")
    public void updatePassword(@Valid @RequestBody ResetPasswordRequestDto passwordRequest,
                               @RequestHeader(USER_PUBLIC_ID) UUID publicId) {
        userService.resetPassword(passwordRequest, publicId);
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
