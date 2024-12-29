package com.another.messageserviceforsraygo.controller;

import com.another.messageserviceforsraygo.entity.DTO.DialogLastMessageDTO;
import com.another.messageserviceforsraygo.entity.Dialog;
import com.another.messageserviceforsraygo.entity.Message;
import com.another.messageserviceforsraygo.service.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message-dialog")
public class DialogController {
    private final DialogService dialogService;

    @Autowired
    public DialogController(DialogService dialogService) {
        this.dialogService = dialogService;
    }

    @GetMapping("/{id}")
    public List<DialogLastMessageDTO> getDialogAndLastMessage(@PathVariable("id") String id) {
        return dialogService.getLastMessage(id);
    }

    @GetMapping("/{id}/{name}")
    public ResponseEntity<Dialog> getFullDialog(@PathVariable("id") String id,
                                               @PathVariable("name") String name) {
        try {
            return ResponseEntity.ok(dialogService.getAllDialog(id, name));
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/{name}")
    public ResponseEntity<Dialog> sendMessage(@PathVariable("id") String id,
                              @PathVariable("name") String name,
                              @RequestBody String message) {
        try {
            return ResponseEntity.ok(dialogService.sendMessage(id, name, message));
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}










