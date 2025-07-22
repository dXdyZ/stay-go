package com.staygo.userservice.service;

import com.staygo.userservice.dto.UserDto;
import com.staygo.userservice.entity.Users;
import com.staygo.userservice.exception.DuplicateUserException;
import com.staygo.userservice.exception.UserNotFoundException;
import com.staygo.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(UserDto userDto) {
        userRepository.save(Users.builder()
                        .email(userDto.getEmail())
                        .phoneNumber(userDto.getPhoneNumber())
                        .username(userDto.getUsername())
                        .publicId(userDto.getPublicId())
                .build());
    }

    public void saveUser(Users users) {
        userRepository.save(users);
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

    public Users getUserByPublicId(UUID publicId) {
        return userRepository.findByPublicId(publicId).orElseThrow(
                () -> new UserNotFoundException("User by public id: %s not found".formatted(publicId.toString()))
        );
    }

    @Transactional
    public Users updateUsername(UUID publicId, String newName) throws DuplicateUserException, UserNotFoundException {
        Users users = getUserByPublicId(publicId);
        if (!userRepository.existsByUsername(newName)) {
            users.setUsername(newName);
            return userRepository.save(users);
        } else throw new DuplicateUserException(String.format("User with name: %s already exists", newName));
    }

    @Transactional
    public Users updatePhoneNumber(UUID publicId, String phoneNumber) throws UserNotFoundException{
        Users users = getUserByPublicId(publicId);
        users.setPhoneNumber(phoneNumber);
        userRepository.save(users);
        return users;
    }

    @Transactional
    public Users updateEmail(UUID publicId, String email) throws DuplicateUserException, UserNotFoundException{
        Users users = getUserByPublicId(publicId);
        if (!userRepository.existsByEmail(email)) {
            users.setEmail(email);
            userRepository.save(users);
            return users;
        } else throw new DuplicateUserException(String.format("User with email: %s already exists", email));
    }
}
