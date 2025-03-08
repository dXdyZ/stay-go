package com.staygo.notificationservice.entity;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationEvent implements Serializable {
    private static final Long UUID = 1L;
    private String email;
    private String confirmCode;
    private EventType eventType;
}
