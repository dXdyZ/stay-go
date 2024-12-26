package com.another.messageserviceforsraygo.service;

import com.another.messageserviceforsraygo.CustomPageable;
import com.another.messageserviceforsraygo.entity.DTO.DialogLastMessageDTO;
import com.another.messageserviceforsraygo.entity.Dialog;
import com.another.messageserviceforsraygo.entity.Message;
import com.another.messageserviceforsraygo.entity.User;
import com.another.messageserviceforsraygo.repository.CustomDialogRepository;
import com.another.messageserviceforsraygo.repository.DialogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<DialogLastMessageDTO> getLastMessage(String userId) {
        Page<Dialog> dialogs = dialogRepository.findByUsers_Id(userId, CustomPageable.getPageable(5));
        if (!dialogs.isEmpty()) {
            return dialogs.stream()
                    .map(d -> {
                        DialogLastMessageDTO dialogDTO = new DialogLastMessageDTO();
                        dialogDTO.setName(d.getUsers().stream().findFirst().toString());
                        dialogDTO.setMessage(d.getMessages().stream()
                                .reduce((first, second) -> second)
                                .toString());
                        return dialogDTO;
                    })
                    .toList();
        }
        return null;
    }

    public Dialog sendMessage(String userId, String name, String message) {
        List<User> users = new ArrayList<>() {
            {
                add(userService.getByName(name));
                add(userService.getUserId(userId));
            }
        };
        if (users.get(0) != null && users.get(1) != null) {
            Optional<Dialog> dialog = dialogRepository.findByUsers(users);
            Message sendMessage = new Message(userId, message, new Date());
            if (dialog.isPresent()) {
                customDialogRepository.addedMessageInDialog(users, sendMessage);
                dialog.get().getMessages().add(sendMessage);
                return dialog.get();
            } else {
                Dialog newDialog = Dialog.builder()
                        .users(users)
                        .messages(List.of(sendMessage))
                        .build();
                dialogRepository.save(newDialog);
            }
        }
        return null;
    }
}
