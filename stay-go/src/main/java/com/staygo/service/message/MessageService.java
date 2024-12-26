package com.staygo.service.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staygo.enity.DTO.message_service.DialogDTO;
import com.staygo.enity.DTO.message_service.DialogLastMessageDTO;
import com.staygo.enity.DTO.message_service.UserMessageDTO;
import com.staygo.enity.user.Users;
import com.staygo.service.user_ser.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MessageService {
    private final RestTemplate restTemplate;
    private final UserService userService;

    private final String url = "http://message-service-for-sray-go";

    @Autowired
    public MessageService(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    public DialogDTO sendMessageForUsers(String id, String username, String message) {
        String sendMessage = url + "/message-dialog/" + id + "/" + username;
        return restTemplate.postForObject(sendMessage, message, DialogDTO.class);
    }

    public List<DialogLastMessageDTO> getMessage(String id) {
        String urlGetDialog = url + "/message-dialog";

        ResponseEntity<List<DialogLastMessageDTO>> responseEntity =
                restTemplate.exchange(
                        url + "/" + id,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<DialogLastMessageDTO>>() {}
                );

        List<DialogLastMessageDTO> userMessageDTOs = responseEntity.getBody();
        if (userMessageDTOs == null) {
            addedUser(id);
        }
        return userMessageDTOs;
    }

    public UserMessageDTO getMessageUserById(String id) {
        String urlGetUser = url + "/message-user";
        UserMessageDTO response = restTemplate.getForObject(url + "/" + id, UserMessageDTO.class);
        if (response == null) {
            return addedUser(id);
        }
        return response;
    }

    private UserMessageDTO addedUser(String id) {
        Users user = userService.findById(Long.valueOf(id)).get();
        restTemplate.postForObject(url + "/message-user/added",
                new UserMessageDTO(String.valueOf(user.getId()), user.getUsername()),
                UserMessageDTO.class);
        return new UserMessageDTO(String.valueOf(user.getId()), user.getUsername());
    }
}
