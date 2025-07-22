package com.lfey.authservice.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record UserAuthInfoDto(
        String username,
        UUID publicId,
        List<String> roles
) implements Serializable {}
