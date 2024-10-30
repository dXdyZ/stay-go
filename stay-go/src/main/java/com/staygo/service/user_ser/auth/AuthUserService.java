package com.staygo.service.user_ser.auth;

import com.staygo.repository.user_repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public AuthUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(users -> new User(
                        users.getUsername(),
                        users.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority(users.getRole().name()))
                )).orElseThrow(() -> new UsernameNotFoundException("Failed user " + username));
    }
}
