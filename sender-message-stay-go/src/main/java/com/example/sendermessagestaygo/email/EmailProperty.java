package com.example.sendermessagestaygo.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "staygo.email")
public class EmailProperty {
    private String username;
    private String host;
    private String password;
    private Integer port;
}