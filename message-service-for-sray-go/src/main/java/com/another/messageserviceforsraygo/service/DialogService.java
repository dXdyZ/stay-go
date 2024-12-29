package com.another.messageserviceforsraygo.service;

import com.another.messageserviceforsraygo.CustomPageable;
import com.another.messageserviceforsraygo.entity.DTO.DialogLastMessageDTO;
import com.another.messageserviceforsraygo.entity.Dialog;
import com.another.messageserviceforsraygo.entity.Message;
import com.another.messageserviceforsraygo.entity.User;
import com.another.messageserviceforsraygo.repository.CustomDialogRepository;
import com.another.messageserviceforsraygo.repository.DialogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                        dialogDTO.setName(d.getUsers().stream().findFirst().get().getName());
                        dialogDTO.setMessage(d.getMessages().stream()
                                .reduce((first, second) -> second)
                                .get().getText());
                        dialogDTO.setTimestamp(d.getMessages().stream().findFirst().get().getTimestamp());
                        return dialogDTO;
                    })
                    .toList();
        }
        return null;
    }


    @Transactional
    public Dialog sendMessage(String userId, String name, String message) throws ChangeSetPersister.NotFoundException {
        List<User> users = userService.getTwoUserByIdAndName(userId, name);
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

    public Dialog getAllDialog(String userId, String username) throws ChangeSetPersister.NotFoundException {
        return dialogRepository.findByUsers(userService.getTwoUserByIdAndName(userId, username))
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
    }
}
