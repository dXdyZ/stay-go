package com.lfey.authservice.dto.kafka;

import java.io.Serializable;

public enum EventType implements Serializable {
    REGISTRATION, PASSWORD_RESET, EMAIL_RESET
}
