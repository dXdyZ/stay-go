package com.example.sendermessagestaygo.receiver;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReceiverTest {

    private static final Logger log = LoggerFactory.getLogger(ReceiverTest.class);
    @Autowired
    private Receiver receiver;

//    @Test
//    void receiveBookingMessage() {
//        log.info("test result from receiver: {}", receiver.receiveBookingMessage().toString());
//    }
}