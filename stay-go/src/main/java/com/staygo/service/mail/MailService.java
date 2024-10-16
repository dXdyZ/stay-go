package com.staygo.service.mail;

import com.staygo.enity.email.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mail.MailHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final MessageChannel emailChannel;
    private final EmailProperties emailProperties;

    @Autowired
    public MailService(MessageChannel emailChannel, EmailProperties emailProperties) {
        this.emailChannel = emailChannel;
        this.emailProperties = emailProperties;
    }

    public void sendMail(String to, String subject, String body) {
        emailChannel.send(
                MessageBuilder.withPayload(body)
                        .setHeader(MailHeaders.TO, to)
                        .setHeader(MailHeaders.SUBJECT, subject)
                        .setHeader(MailHeaders.FROM, emailProperties.getUsername())
                        .build()
        );
    }
}
