package com.staygo.service.message;

import com.staygo.enity.DTO.message_service.DialogDTO;
import com.staygo.enity.DTO.message_service.DialogLastMessageDTO;
import com.staygo.enity.DTO.message_service.UserMessageDTO;
import com.staygo.enity.user.Users;
import com.staygo.service.user_ser.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MessageService {
    private final RestTemplate restTemplate;
    private final UserService userService;

    private final String url = "http://message-service-for-sray-go";
    private final UserCachedForSendMessage userCachedForSendMessage;

    @Autowired
    public MessageService(RestTemplate restTemplate, UserService userService, UserCachedForSendMessage userCachedForSendMessage) {
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.userCachedForSendMessage = userCachedForSendMessage;
    }

    public DialogDTO sendMessageForUsers(Principal principal, String username, String message)
            throws ChangeSetPersister.NotFoundException {
        Users users = userCachedForSendMessage.getUserByPrincipal(principal);
        String sendMessage = url + "/message-dialog/" + users.getId() + "/" + username;
        DialogDTO dialogDTO = null;
        try {
            dialogDTO = restTemplate.postForObject(sendMessage, message, DialogDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            restTemplate.postForObject(url + "/message-user/add-group", new ArrayList<Users>(){{
                add(userService.findById(users.getId()).get());
                add(userService.findByName(username).orElseThrow(ChangeSetPersister.NotFoundException::new));
            }}, Void.class);
        }
        return dialogDTO;
    }

    public List<DialogLastMessageDTO> getMessage(Principal principal) {
        String urlGetDialog = url + "/message-dialog";
        Users users = userCachedForSendMessage.getUserByPrincipal(principal);
        ResponseEntity<List<DialogLastMessageDTO>> responseEntity =
                restTemplate.exchange(
                        urlGetDialog + "/" + users.getId(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        List<DialogLastMessageDTO> userMessageDTOs = responseEntity.getBody();
        if (userMessageDTOs == null) {
            addedUser(users);
        }
        return userMessageDTOs;
    }

    public DialogDTO getFullDialog(Principal principal, String username) {
        Users users = userCachedForSendMessage.getUserByPrincipal(principal);
        String urlGetFulDialog = url + "/message-user/" + users.getId() + "/" + username;
        try {
            return restTemplate.getForObject(urlGetFulDialog, DialogDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }

    public UserMessageDTO getMessageUserById(Principal principal) {
        String urlGetUser = url + "/message-user";
        Users users = userCachedForSendMessage.getUserByPrincipal(principal);
        UserMessageDTO response = restTemplate.getForObject(urlGetUser + "/" + users.getId(), UserMessageDTO.class);
        if (response == null) {
            return addedUser(users);
        }
        return response;
    }

    private UserMessageDTO addedUser(Users users) {
        restTemplate.postForObject(url + "/message-user/added",
                new UserMessageDTO(String.valueOf(users.getId()), users.getUsername()),
                UserMessageDTO.class);
        return new UserMessageDTO(String.valueOf(users.getId()), users.getUsername());
    }
}
