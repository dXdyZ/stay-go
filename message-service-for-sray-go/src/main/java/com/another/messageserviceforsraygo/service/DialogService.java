package com.another.messageserviceforsraygo.service;

import com.another.messageserviceforsraygo.CustomPageable;
import com.another.messageserviceforsraygo.entity.Dialog;
import com.another.messageserviceforsraygo.entity.Message;
import com.another.messageserviceforsraygo.entity.User;
import com.another.messageserviceforsraygo.repository.CustomDialogRepository;
import com.another.messageserviceforsraygo.repository.DialogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DialogService {
    private final DialogRepository dialogRepository;
    private final CustomDialogRepository customDialogRepository;
    private final UserService userService;

    @Autowired
    public DialogService(DialogRepository dialogRepository, CustomDialogRepository customDialogRepository, UserService userService) {
        this.dialogRepository = dialogRepository;
        this.customDialogRepository = customDialogRepository;
        this.userService = userService;
    }

    public Dialog sendMessage(String userId, String name, String message) {
        List<User> users = new ArrayList<>() {
            {
                add(userService.getUserId(userId));
                add(userService.getByName(name));
            }
        };
        if (users.get(0) != null && users.get(0) != null) {
            customDialogRepository.addedMessageInDialog(users, new Message(userId, message, new Date()));
            Page<Dialog> dialogs = dialogRepository.findByUsers(users, CustomPageable.getPageable(10));
            return dialogs.getContent().isEmpty() ? null : dialogs.getContent().get(0);
        }
        return null;
    }
}
