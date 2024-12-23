package com.another.messageserviceforsraygo.service;

import com.another.messageserviceforsraygo.entity.Friend;
import com.another.messageserviceforsraygo.entity.FriendStatus;
import com.another.messageserviceforsraygo.entity.User;
import com.another.messageserviceforsraygo.repository.CustomUserRepository;
import com.another.messageserviceforsraygo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CustomUserRepository customUserRepository;

    @Autowired
    public UserService(UserRepository userRepository, CustomUserRepository customUserRepository) {
        this.userRepository = userRepository;
        this.customUserRepository = customUserRepository;
    }

    @Transactional
    public void friendAdd(String userId, String name) {
        userRepository.findByName(name).ifPresent(user -> {
            customUserRepository.updateUserFriend(userId, "friends",
                    new Friend(user.getId(), FriendStatus.ACTIVE.toString(), new Date()));
        });
    }

    public void deletingFriend(String userId, String name) {
        customUserRepository.removeFriendByName(userId, name);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getByName(String name) {
        return userRepository.findByName(name).orElse(new User());
    }

    public User getUserId(String id) {
        return userRepository.findById("id").orElse(new User());
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }
}
