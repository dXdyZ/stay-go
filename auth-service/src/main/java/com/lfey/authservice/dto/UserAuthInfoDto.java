package com.lfey.authservice.dto;

import java.io.Serializable;
import java.util.List;

public record UserAuthInfoDto(
        String username,
        List<String> roles
) implements Serializable {}
