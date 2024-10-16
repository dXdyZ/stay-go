package com.staygo.configuration;

import com.staygo.enity.email.EmailProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Properties;

@Slf4j
@Configuration
public class MailSender {
    private final EmailProperties emailProperties;

    @Autowired
    public MailSender(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    @Bean
    public JavaMailSender customMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailProperties.getHost());

        if (emailProperties.getPort() != null) {
            mailSender.setPort(emailProperties.getPort());
        } else {
            log.error("get port: {}", emailProperties.getPort());
            log.warn("get data email properties: {}", emailProperties.toString());
            mailSender.setPort(465);
        }

        mailSender.setUsername(emailProperties.getUsername());
        mailSender.setPassword(emailProperties.getPassword());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.debug", "true");

        return mailSender;
    }
}
