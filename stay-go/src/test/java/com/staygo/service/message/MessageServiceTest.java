package com.staygo.service.message;

import com.staygo.enity.DTO.message_service.UserMessageDTO;
import com.staygo.enity.user.Users;
import com.staygo.service.user_ser.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
class MessageServiceTest {
    private static final Logger log = LoggerFactory.getLogger(MessageServiceTest.class);
    @Autowired
    private MessageService messageService;

    @MockBean
    private UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testRequestForMessageService() {
        Users users = new Users(3L, "hello", "hello@gmail.com", "123412",
                null, null, null, null);
        when(userService.findById(3L)).thenReturn(Optional.of(users));
        UserMessageDTO userMessageDTO = messageService.getMessageUserById(String.valueOf(3L));
        log.info("message users: {}", userMessageDTO.toString());
    }
}