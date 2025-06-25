package com.example.staygoclient.controller;

import com.example.staygoclient.clietn.AuthClient;
import com.example.staygoclient.dto.*;
import com.example.staygoclient.exception.ApiErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class AuthController {
    private final AuthClient authClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String COOKIE_NAME = "auth_cookie";

    public AuthController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDto",
                new RegistrationDto("", "", "", ""));
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute RegistrationDto registrationDto, Model model, HttpSession session) {
       try {
           authClient.registration(registrationDto);
           session.setAttribute("confirmRegistration", registrationDto.email());
           return "redirect:/registration/confirm";
       } catch (ApiErrorException exception) {
           ErrorResponse errorResponse = exception.getErrorResponse();
           model.addAttribute("error", errorResponse.error().get("message"));
       }
       return "registration";
    }


    @GetMapping("/registration/confirm")
    public String showConfirmRegistration(Model model) {
        model.addAttribute("error", null);
        return "verification";
    }

    @PostMapping("/registration/confirm")
    public String registrationConfirm(@RequestParam("code") String code, Model model, HttpServletResponse response,
                                      HttpSession session) {
        try {
            JwtTokenDto jwt = authClient.registrationConfirm(new ConfirmRegistrationDto(
                    (String) session.getAttribute("confirmRegistration"), code));
            ObjectNode token = objectMapper.createObjectNode();
            token.put("accessToken", jwt.accessToken());
            token.put("refreshToken", jwt.refreshToken());
            addCookie(response, COOKIE_NAME, token.toString(), Integer.MAX_VALUE);
            return "redirect:/";
        } catch (ApiErrorException exception) {
            ErrorResponse errorResponse = exception.getErrorResponse();
            model.addAttribute("error", errorResponse.error().get("message"));
        }
        return "verification";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("loginDto", new LoginDto("", ""));
        model.addAttribute("error", false);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDto loginDto, Model model, HttpServletResponse response) {
        try {
            JwtTokenDto jwt = authClient.login(loginDto);
            ObjectNode token = objectMapper.createObjectNode();
            token.put("accessToken", jwt.accessToken());
            token.put("refreshToken", jwt.refreshToken());
            addCookie(response, COOKIE_NAME, token.toString(), Integer.MAX_VALUE);
            return "redirect:/";
        } catch (ApiErrorException exception) {
            ErrorResponse errorResponse = exception.getErrorResponse();
            model.addAttribute("error", errorResponse.error().get("message"));
        }
        return "login";
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); //Установить true если используется https
        response.addCookie(cookie);
    }
}
