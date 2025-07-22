package com.lfey.authservice.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final UUID publicId;

    private final String username;
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    private final Boolean accountEnable;
    private final Boolean accountLocked;

    public CustomUserDetails(UUID publicId, String username, String password,
                             Collection<? extends GrantedAuthority> authorities, Boolean accountEnable, Boolean accountLocked) {
        this.publicId = publicId;
        this.username = username;
        this.password = password;
        this.accountEnable = accountEnable;
        this.accountLocked = accountLocked;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return accountEnable;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountLocked;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

}
