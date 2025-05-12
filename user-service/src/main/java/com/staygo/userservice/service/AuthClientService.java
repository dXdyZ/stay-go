package com.staygo.userservice.service;

import com.staygo.userservice.client.AuthClient;
import com.staygo.userservice.dto.RoleRequestDto;
import com.staygo.userservice.exception.DuplicateRoleException;
import feign.FeignException;
import org.springframework.stereotype.Service;

@Service
public class AuthClientService {
    private final AuthClient authClient;

    public AuthClientService(AuthClient authClient) {
        this.authClient = authClient;
    }

    public void addRoleInUserService(String username, String role) {
        try {
            authClient.addRole(username, new RoleRequestDto(role));
        } catch (FeignException.BadRequest badRequest) {
            throw new DuplicateRoleException(String.format("Role: %s the user already has it", role));
        }
    }
}
