package com.lfey.authservice.entity.redis;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "EmailUpdate", timeToLive = 900)
public class EmailUpdate {
    @Id
    private String newEmail;
    private UUID publicId;
    private String code;
}
