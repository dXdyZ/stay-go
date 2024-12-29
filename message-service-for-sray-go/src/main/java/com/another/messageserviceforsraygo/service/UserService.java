package com.another.messageserviceforsraygo.service;

import com.another.messageserviceforsraygo.entity.Friend;
import com.another.messageserviceforsraygo.entity.FriendStatus;
import com.another.messageserviceforsraygo.entity.User;
import com.another.messageserviceforsraygo.repository.CustomUserRepository;
import com.another.messageserviceforsraygo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
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

    //Изменить, так как будет удаление всего списка скорее всего
    @Deprecated
    public void deletingFriend(String userId, String name) {
        customUserRepository.removeFriendByName(userId, name);
    }

    public List<User> getTwoUserByIdAndName(String userId, String username) throws ChangeSetPersister.NotFoundException {
        return new ArrayList<>() {
            {
                add(userRepository.findByName(username).orElseThrow(ChangeSetPersister.NotFoundException::new));
                add(userRepository.findById(userId).orElseThrow(ChangeSetPersister.NotFoundException::new));
            }
        };
    }

    public List<User> getUserByListId(List<String> ids) {
        return userRepository.findByIdIn(ids);
    }

    public void saveGroupUsers(List<User> users) {
        Set<String> ids = users.stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        List<User> getUsers = userRepository.findByIdIn(new ArrayList<>(ids));
        if (!getUsers.isEmpty()) {
            users.removeIf(id -> id.getId().equals(getUsers.iterator().next().getId()));
            userRepository.saveAll(users);
        } else userRepository.saveAll(users);
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getByName(String name) {
        return userRepository.findByName(name).orElse(null);
    }

    public User getUserId(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }
}
