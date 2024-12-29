package com.staygo.controller;

import com.staygo.enity.DTO.message_service.DialogDTO;
import com.staygo.enity.DTO.message_service.DialogLastMessageDTO;
import com.staygo.service.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageServiceController {
    private final MessageService messageService;

    @Autowired
    public MessageServiceController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public List<DialogLastMessageDTO> getDialogs(Principal principal) {
        return messageService.getMessage(principal);
    }

    @GetMapping("/send/{username}")
    public ResponseEntity<DialogDTO> sendMessage(@PathVariable("username") String username,
                                                @RequestBody String message, Principal principal) {
        try {
            return ResponseEntity.ok(messageService.sendMessageForUsers(principal, username, message));
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);я
        }
    }

    @GetMapping("/{username}")
    public DialogDTO getFullDialog(@PathVariable String username, Principal principal) {
        return messageService.getFullDialog(principal, username);
    }
}













