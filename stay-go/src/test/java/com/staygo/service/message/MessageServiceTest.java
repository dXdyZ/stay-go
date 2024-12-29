package com.staygo.service.message;

import com.staygo.enity.DTO.message_service.DialogDTO;
import com.staygo.enity.DTO.message_service.DialogLastMessageDTO;
import com.staygo.enity.DTO.message_service.UserMessageDTO;
import com.staygo.enity.user.Users;
import com.staygo.service.user_ser.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@SpringBootTest
class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserCachedForSendMessage userCachedForSendMessage;

    @MockBean
    private RestTemplate restTemplate;

    private final String testUrl = "http://message-service-for-sray-go";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMessageForUsersTest() throws Exception {
        // Arrange
        Principal principal = () -> "testUser";
        Users currentUser = new Users(1L, "testUser", "test@example.com", "1234", null, null, null, null);
        Users recipient = new Users(2L, "recipientUser", "recipient@example.com", "5678", null, null, null, null);

        when(userCachedForSendMessage.getUserByPrincipal(principal)).thenReturn(currentUser);
        when(userService.findById(currentUser.getId())).thenReturn(Optional.of(currentUser));
        when(userService.findByName("recipientUser")).thenReturn(Optional.of(recipient));

        DialogDTO mockDialog = new DialogDTO();
        when(restTemplate.postForObject(
                eq(testUrl + "/message-dialog/" + currentUser.getId() + "/recipientUser"),
                eq("testMessage"),
                eq(DialogDTO.class)
        )).thenReturn(mockDialog);

        // Act
        DialogDTO result = messageService.sendMessageForUsers(principal, "recipientUser", "testMessage");

        // Assert
        assertNotNull(result);
        verify(restTemplate, times(1)).postForObject(
                eq(testUrl + "/message-dialog/" + currentUser.getId() + "/recipientUser"),
                eq("testMessage"),
                eq(DialogDTO.class)
        );
    }

    @Test
    void getMessageTest() {
        // Arrange
        Principal principal = () -> "testUser";
        Users currentUser = new Users(1L, "testUser", "test@example.com", "1234", null, null, null, null);
        when(userCachedForSendMessage.getUserByPrincipal(principal)).thenReturn(currentUser);

        List<DialogLastMessageDTO> mockMessages = List.of(new DialogLastMessageDTO());
        ResponseEntity<List<DialogLastMessageDTO>> responseEntity =
                new ResponseEntity<>(mockMessages, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(testUrl + "/message-dialog/" + currentUser.getId()),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        List<DialogLastMessageDTO> result = messageService.getMessage(principal);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(restTemplate, times(1)).exchange(
                eq(testUrl + "/message-dialog/" + currentUser.getId()),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void getFullDialogTest() {
        // Arrange
        Principal principal = () -> "testUser";
        Users currentUser = new Users(1L, "testUser", "test@example.com", "1234", null, null, null, null);
        when(userCachedForSendMessage.getUserByPrincipal(principal)).thenReturn(currentUser);

        DialogDTO mockDialog = new DialogDTO();
        when(restTemplate.getForObject(
                eq(testUrl + "/message-user/" + currentUser.getId() + "/recipientUser"),
                eq(DialogDTO.class)
        )).thenReturn(mockDialog);

        // Act
        DialogDTO result = messageService.getFullDialog(principal, "recipientUser");

        // Assert
        assertNotNull(result);
        verify(restTemplate, times(1)).getForObject(
                eq(testUrl + "/message-user/" + currentUser.getId() + "/recipientUser"),
                eq(DialogDTO.class)
        );
    }

    @Test
    void getMessageUserByIdTest() {
        // Arrange
        Principal principal = () -> "testUser";
        Users currentUser = new Users(1L, "testUser", "test@example.com", "1234", null, null, null, null);
        when(userCachedForSendMessage.getUserByPrincipal(principal)).thenReturn(currentUser);

        UserMessageDTO mockUserMessageDTO = new UserMessageDTO("1", "testUser");
        when(restTemplate.getForObject(
                eq(testUrl + "/message-user/" + currentUser.getId()),
                eq(UserMessageDTO.class)
        )).thenReturn(mockUserMessageDTO);

        // Act
        UserMessageDTO result = messageService.getMessageUserById(principal);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("testUser", result.getName());
        verify(restTemplate, times(1)).getForObject(
                eq(testUrl + "/message-user/" + currentUser.getId()),
                eq(UserMessageDTO.class)
        );
    }
}










