package com.example.sendermessagestaygo.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SendBookingMailTest {

    private static final Logger log = LoggerFactory.getLogger(SendBookingMailTest.class);
    @Autowired
    private SendBookingMail sendBookingMail;

    @Test
    void sendMail() {
        log.info("result get message from rabbit: {}", sendBookingMail.sendMail());
    }
}