package com.lfey.authservice.service.clients;

import com.lfey.authservice.dto.UserDetailsDto;
import com.lfey.authservice.exception.ServerErrorException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserClientService {
    private final UserClient userClient;

    @Autowired
    public UserClientService(UserClient userClient) {
        this.userClient = userClient;
    }

    public void userRegistrationInUserService(UserDetailsDto userDetailsDto) {
        userClient.saveUser(userDetailsDto);
    }


    //TODO Скоре всего придется делать его асинхронным в случае пустого кеша чтобы пользовать не ожидал проверки
    @Cacheable(value = "UserByEmail", key = "#email")
    public UserDetailsDto getUserByEmailFromUserService(String email) {
        try {
            return userClient.getUserByEmail(email);
        } catch (FeignException.BadRequest badRequest) {
            return null;
        }
    }

    public UserDetailsDto getUserByUsernameFromUserService(String username) {
        return userClient.getUserByUsername(username);
    }

    @Cacheable(value = "UserByPublicId", key = "#publicId")
    public UserDetailsDto getUserByPublicIdFromUserService(UUID publicId) {
        return userClient.getUserByPublicId(publicId);
    }

    @CachePut(value = "UserByEmail", key = "#result.email")
    public UserDetailsDto updateUsernameInUserService(String newUsername, UUID publicId) throws ServerErrorException{
        try {
            return userClient.updateUsername(newUsername, publicId);
        } catch (FeignException.BadRequest badRequest) {
            throw new ServerErrorException("An error occurred when updating the username");
        }
    }

    public UserDetailsDto updateUserEmailInUserService(String newEmail, UUID publicId) throws ServerErrorException{
        try {
            return userClient.updateEmail(newEmail, publicId);
        } catch (FeignException.BadRequest badRequest) {
            throw new ServerErrorException("An error occurred when updating the email");
        }
    }
}
