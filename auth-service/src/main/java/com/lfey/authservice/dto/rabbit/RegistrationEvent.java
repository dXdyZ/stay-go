package com.lfey.authservice.dto.rabbit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationEvent {
    private static final Long UUID = 1L;
    private String email;
    private String confirmCode;
    private EventType eventType;
}
