package com.lfey.authservice.controller;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.entity.UserReg;
import com.lfey.authservice.service.AuthService;
import com.lfey.authservice.service.UserService;
import com.lfey.authservice.validation.RegistrationGroup;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public final static String USERNAME_HEADER = "X-User-Username";

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public void registerUser(@Validated(RegistrationGroup.class) @RequestBody UserReg userReg) {
        authService.registerUser(userReg);
    }

    @PostMapping("/confirm-registration")
    public ResponseEntity<JwtToken> validationUser(@RequestBody ValidationCode validationCode) {
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build().toUri()).body(authService.getJWT(validationCode));
    }

    //TODO Узнать в каких куках будет все храниться и изменить
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("refreshToken") String refreshToken,
                                        HttpServletResponse response) {
        authService.logout(refreshToken);

        var cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/api/auth");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("/confirm-email-update")
    public ResponseEntity<UserDto> validationUpdateEmail(@RequestBody ValidationCode validationCode,
                                         @RequestHeader(USERNAME_HEADER) String username) {
        return ResponseEntity.ok(userService.updateEmailInUserService(validationCode, username));
    }
}
