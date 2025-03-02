package com.lfey.authservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "UserReg", timeToLive = 900)
public class UserReg {
    @Id
    @Email
    private String email;

    @Size(min = 4, max = 20)
    private String username;

    @Size(min = 8, max = 50)
    private String password;

    @NotNull
    private String phoneNumber;

    private String code;
}
