//package com.example.staygoclient.configuration;
//
//import com.example.staygoclient.security.JwtAuthentication;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//
//@Slf4j
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        SecurityContextHolder.clearContext();
//
//        String cookieValue = getCookieValue(request, "auth_cookie");
//        log.info("Get cookie: {}", cookieValue);
//
//
//        if (cookieValue != null) {
//            try {
//                byte[] decodedBytes = Base64.getDecoder().decode(cookieValue);
//                String decodedJson = new String(decodedBytes, StandardCharsets.UTF_8);
//                log.info("Cookie before decoding: {}", decodedJson);
//
//                JsonNode tokenNode = objectMapper.readTree(decodedJson);
//                String accessToken = tokenNode.path("accessToken").asText(null);
//                String refreshToken = tokenNode.path("refreshToken").asText(null);
//
//                if (accessToken != null) {
//                    JwtAuthentication auth = new JwtAuthentication(accessToken, refreshToken);
//                    SecurityContextHolder.getContext().setAuthentication(auth);
//                }
//            } catch (IOException ex) {
//
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    private String getCookieValue(HttpServletRequest request, String name) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(name)) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
//}
