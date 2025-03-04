package com.staygo.userservice.service;

import com.staygo.userservice.dto.UserDto;
import com.staygo.userservice.entity.Users;
import com.staygo.userservice.exception.DuplicateUserException;
import com.staygo.userservice.exception.UserNotFoundException;
import com.staygo.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(UserDto userDto) {
        userRepository.save(Users.builder()
                        .email(userDto.getEmail())
                        .phoneNumber(userDto.getPhoneNumber())
                        .username(userDto.getUsername())
                .build());
    }

    public Users getUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format("User by id: %s not found", id)));
    }

    public Users getUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(String.format("User by username: %s not found", username))
        );
    }

    public Users getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(String.format("User by email: %s not found", email))
        );
    }

    public Users getUserByPhoneNumber(String phoneNumber) throws UserNotFoundException {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new UserNotFoundException(String.format("User by phone number: %s not found", phoneNumber))
        );
    }

    @Transactional
    public Users updateUsername(String oldName, String newName) throws DuplicateUserException{
        Users users = userRepository.findByUsername(oldName).orElseThrow(
                () -> new UserNotFoundException(String.format("User by username: %s not found", oldName))
        );
        if (!userRepository.existsByUsername(newName)) {
            users.setUsername(newName);
            return userRepository.save(users);
        } else throw new DuplicateUserException(String.format("User with name: %s already exists", newName));
    }

    @Transactional
    public Users updatePhoneNumber(String username, String phoneNumber) {
        Users users = userRepository.findByUsername(username).get();
        users.setPhoneNumber(phoneNumber);
        return userRepository.save(users);
    }

    //TODO Сделать обновление email
    @Transactional
    public Users updateEmail(String username, String email) throws DuplicateUserException{
        Users users = userRepository.findByUsername(username).get();
        if (!userRepository.existsByEmail(email)) {
            users.setEmail(email);
            return users;
        } else throw new DuplicateUserException(String.format("User with email: %s already exists", email));
    }
}
