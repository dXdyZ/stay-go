package com.staygo.service.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageService {
    private final RestTemplate restTemplate;

    @Autowired
    public MessageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getMessageById(String id) {
        String response = restTemplate.getForObject(
                "http://message-service-for-sray-go/message-user/1",
                String.class
        );
        return response;
    }
}
