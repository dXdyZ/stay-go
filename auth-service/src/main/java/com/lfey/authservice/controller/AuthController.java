package com.lfey.authservice.controller;

import com.lfey.authservice.dto.JwtToken;
import com.lfey.authservice.dto.UserReg;
import com.lfey.authservice.dto.ValidationCode;
import com.lfey.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
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
        return userService.saveUser(validationCode);
    }
}
