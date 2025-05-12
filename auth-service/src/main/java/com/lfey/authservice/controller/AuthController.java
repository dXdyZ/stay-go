package com.lfey.authservice.controller;

import com.lfey.authservice.controller.documentation.AuthControllerDocs;
import com.lfey.authservice.dto.*;
import com.lfey.authservice.service.AuthService;
import com.lfey.authservice.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/auth/")
@Tag(name = "Authenticated API", description = "Management authentication")
public class AuthController implements AuthControllerDocs {
    public final static String USERNAME_HEADER = "X-User-Username";

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/registration")
    public void registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDTO) {
        authService.registerUser(userRegistrationDTO);
    }

   @PostMapping("/registration/confirm")
    public ResponseEntity<JwtTokenDto> validationUser(@Valid @RequestBody ValidationCodeDto validationCodeDto) {
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build().toUri()).body(authService.getJWT(validationCodeDto));
    }

    //TODO Узнать в каких куках будет все храниться и изменить
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("refreshToken") String refreshToken,
                                        HttpServletResponse response) {
        authService.logout(refreshToken);

        var cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> login(@Valid @RequestBody AuthRequestDto authRequestDto) {
        return ResponseEntity.ok(authService.login(authRequestDto));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<JwtTokenDto> refreshAccessToken(@Valid @RequestBody JwtTokenDto jwtTokenDto) {
        return ResponseEntity.ok(authService.refreshAccessToken(jwtTokenDto));
    }

    @PostMapping("/email-update/confirm")
    public ResponseEntity<UserDetailsDto> validationUpdateEmail(@Valid @RequestBody ValidationCodeDto validationCodeDto,
                                                                @RequestHeader(USERNAME_HEADER) String username) {
        return ResponseEntity.ok(userService.updateEmailInUserService(validationCodeDto, username));
    }
}
