package com.lfey.authservice.service.clients;

import com.lfey.authservice.dto.UserDto;
import com.lfey.authservice.exception.ServerErrorException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserClientService {
    private final UserClient userClient;

    @Autowired
    public UserClientService(UserClient userClient) {
        this.userClient = userClient;
    }

    public void userRegistrationInUserService(UserDto userDto) {
        userClient.saveUser(userDto);
    }


    //TODO Скоре всего придется делать его асинхронным в случае пустого кеша чтобы пользовать не ожидал проверки
    @Cacheable(value = "userByEmail", key = "#email")
    public UserDto getUserByEmailFromUserService(String email) {
        try {
            return userClient.getUserByEmail(email);
        } catch (FeignException.BadRequest badRequest) {
            return null;
        }
    }

    public UserDto getUserByUsernameFromUserService(String username) {
        return userClient.getUserByUsername(username);
    }

    @CachePut(value = "userByEmail", key = "#result.email")
    public UserDto updateUsernameInUserService(String newUsername, String username) throws ServerErrorException{
        try {
            return userClient.updateUsername(newUsername, username);
        } catch (FeignException.BadRequest badRequest) {
            throw new ServerErrorException("An error occurred when updating the username");
        }
    }

    public UserDto updateUserEmailInUserService(String newEmail, String username) throws ServerErrorException{
        try {
            return userClient.updateEmail(newEmail, username);
        } catch (FeignException.BadRequest badRequest) {
            throw new ServerErrorException("An error occurred when updating the email");
        }
    }
}
