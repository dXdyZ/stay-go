package com.another.messageserviceforsraygo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "dialog")
public class Dialog {
    @Id
    private String dialogId;
    private List<User> users = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
}
