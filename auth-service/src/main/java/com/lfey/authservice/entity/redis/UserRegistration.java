package com.lfey.authservice.entity.redis;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "UserRegistration", timeToLive = 900)
public class UserRegistration implements Serializable {
    @Id
    private String email;
    private String username;
    private String password;
    private String phoneNumber;
    private String code;
}
