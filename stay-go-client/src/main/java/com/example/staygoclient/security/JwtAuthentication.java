//package com.example.staygoclient.security;
//
//import lombok.Data;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.util.Collection;
//
//public class JwtAuthentication extends AbstractAuthenticationToken {
//
//    @Getter
//    @Setter
//    private String accessToken;
//    @Getter
//    @Setter
//    private String refreshToken;
//
//    public JwtAuthentication(String accessToken, String refreshToken) {
//        super(null);
//        this.accessToken = accessToken;
//        this.refreshToken = refreshToken;
//        setAuthenticated(true);
//    }
//
//    @Override
//    public Object getCredentials() {
//        return null;
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return accessToken;
//    }
//}
