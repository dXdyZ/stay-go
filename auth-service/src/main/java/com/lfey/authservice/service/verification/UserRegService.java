package com.lfey.authservice.service.verification;

import com.lfey.authservice.dto.UserReg;
import com.lfey.authservice.exception.UserRegNotFoundException;
import com.lfey.authservice.repository.cache.UserRegRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRegService {
    private final UserRegRepository userRegRepository;

    @Autowired
    public UserRegService(UserRegRepository userRegRepository) {
        this.userRegRepository = userRegRepository;
    }

    public void saveUserData(UserReg userReg) {
        userRegRepository.save(userReg);
    }

    public UserReg getUserRegByEmail(String email) {
        return userRegRepository.findById(email).orElseThrow(() -> new UserRegNotFoundException("Code is not valid"));
    }

    public Boolean existsByEmail(String email) {
        return userRegRepository.existsByEmail(email);
    }

    public void removeUserRegByEmail(String email) {
        userRegRepository.deleteById(email);
    }
}
