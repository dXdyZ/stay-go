package com.example.sendermessagestaygo.service;

import com.example.sendermessagestaygo.email.EmailProperty;
import org.springframework.integration.mail.MailHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final MessageChannel messageChannel;
    private final EmailProperty emailProperty;

    public MailService(MessageChannel messageChannel, EmailProperty emailProperty) {
        this.messageChannel = messageChannel;
        this.emailProperty = emailProperty;
    }

    public void sendMail(String to, String subject, String body) {
        messageChannel.send(
                MessageBuilder.withPayload(body)
                        .setHeader(MailHeaders.TO, to)
                        .setHeader(MailHeaders.SUBJECT, subject)
                        .setHeader(MailHeaders.FROM, emailProperty.getUsername())
                        .build()
        );
    }
}
