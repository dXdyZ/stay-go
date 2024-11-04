package com.example.sendermessagestaygo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mail.MailSendingMessageHandler;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class EmailConfig {

    @Bean
    public MessageChannel messageChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "messageChannel")
    public MessageHandler mailOutBoundAdapter(JavaMailSender mailSender) {
        return new MailSendingMessageHandler(mailSender);
    }
}
