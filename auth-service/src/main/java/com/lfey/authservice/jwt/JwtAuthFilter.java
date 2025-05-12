package com.lfey.authservice.jwt;

import com.lfey.authservice.controller.GlobalExceptionHandler;
import com.lfey.authservice.exception.InvalidJwtTokenException;
import com.lfey.authservice.exception.TokenRevokedException;
import com.lfey.authservice.service.security_service.ValidationRefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final ValidationRefreshTokenService validationRefreshTokenService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtils jwtUtils, ValidationRefreshTokenService validationRefreshTokenService,
                         UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.validationRefreshTokenService = validationRefreshTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring("Bearer ".length());

        try {
            if (!jwtUtils.validationToken(token)) {
                throw new InvalidJwtTokenException("Invalid or expired JWT token");
            }
            if (!validationRefreshTokenService.isValidToken(token)) {
                throw new TokenRevokedException("Token was revoked");
            }

            UserDetails userDetails = userDetailsService
                                      .loadUserByUsername(jwtUtils.extractUsername(token));

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (InvalidJwtTokenException | TokenRevokedException ex) {
            sendErrorResponse(response, ex.getMessage(), HttpStatus.UNAUTHORIZED);
            return;
        } catch (Exception ex) {
            sendErrorResponse(response, "Authentication failed", HttpStatus.UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }


    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus status) throws IOException{
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        response.getWriter().write(
                new GlobalExceptionHandler.ErrorResponse(
                        Instant.now(),
                        Map.of("error", message),
                        status.value()
                ).toJson()
        );
    }
}









