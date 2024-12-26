package com.another.messageserviceforsraygo.controller;

import com.another.messageserviceforsraygo.entity.DTO.DialogLastMessageDTO;
import com.another.messageserviceforsraygo.entity.Dialog;
import com.another.messageserviceforsraygo.entity.Message;
import com.another.messageserviceforsraygo.service.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/{id}/{name}")
    public Dialog sendMessage(@PathVariable("id") String id,
                              @PathVariable("name") String name,
                              @RequestBody String message) {
        return dialogService.sendMessage(id, name, message);
    }
}










