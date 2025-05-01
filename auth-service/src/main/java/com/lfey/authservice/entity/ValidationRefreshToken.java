package com.lfey.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "ValidRefToken", timeToLive = 604800000)
public class ValidationRefreshToken implements Serializable {

    @Id
    private String token;

    private String username;
}
