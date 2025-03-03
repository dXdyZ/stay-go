package com.lfey.authservice.service.clients;

import com.lfey.authservice.dto.UserDto;
import feign.Feign;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
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


    //TODO Сделать обновление кеша в случае изменения данных пользователя
    @Cacheable(value = "userByEmail", key = "#email")
    public UserDto getUserByEmailFromUserService(String email) {
        try {
            return userClient.getUserByEmail(email);
        } catch (FeignException.BadRequest badRequest) {
            return null;
        }
    }
}
