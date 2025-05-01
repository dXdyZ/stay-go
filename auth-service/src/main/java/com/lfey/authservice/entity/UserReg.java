package com.lfey.authservice.entity;


import com.lfey.authservice.validation.RegistrationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@RedisHash(value = "UserReg", timeToLive = 900)
public class UserReg implements Serializable {

    @Id
    @Email(groups = RegistrationGroup.class)
    private String email;

    @NotBlank(groups = RegistrationGroup.class)
    private String username;

    @NotBlank(groups = RegistrationGroup.class)
    private String password;

    @NotBlank(groups = RegistrationGroup.class)
    private String phoneNumber;

    private String code;
}
