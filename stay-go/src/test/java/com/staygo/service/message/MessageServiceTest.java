package com.staygo.service.message;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageServiceTest {
    private static final Logger log = LoggerFactory.getLogger(MessageServiceTest.class);
    @Autowired
    private MessageService messageService;


    @Test
    public void testRequestForMessageService() {
        log.info("message service response: {}", messageService.getMessageById("1"));
    }
}