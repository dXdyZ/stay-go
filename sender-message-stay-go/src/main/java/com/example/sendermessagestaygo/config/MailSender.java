package com.example.sendermessagestaygo.config;

import com.example.sendermessagestaygo.email.EmailProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSender {
    private final EmailProperty emailProperty;

    @Autowired
    public MailSender(EmailProperty emailProperty) {
        this.emailProperty = emailProperty;
    }

    @Bean
    public JavaMailSender customMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailProperty.getHost());

        if (emailProperty.getPort() != null) {
            mailSender.setPort(emailProperty.getPort());
        } else {
            mailSender.setPort(465);
        }

        mailSender.setUsername(emailProperty.getUsername());
        mailSender.setPassword(emailProperty.getPassword());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.debug", "true");

        return mailSender;
    }
}
