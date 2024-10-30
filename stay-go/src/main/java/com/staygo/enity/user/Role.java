package com.staygo.enity.user;


import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public enum Role implements Serializable, GrantedAuthority {
    ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR, ROLE_MANAGER;

    @Override
    public String getAuthority() {
        return name();
    }
}
