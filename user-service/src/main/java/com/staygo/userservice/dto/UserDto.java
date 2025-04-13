package com.staygo.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private static final Long UUID = 1L;
    private String email;
    private String phoneNumber;
    private String username;
}