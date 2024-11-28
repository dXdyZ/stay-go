package com.example.sendermessagestaygo.config;

import com.example.sendermessagestaygo.enity.ArmoredRoomDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;

import java.io.File;

@Configuration
public class FileWriterIntegrationConfig {
    @Bean
    public IntegrationFlow fileWriterFlow(ObjectMapper objectMapper) {
        return IntegrationFlow
                .from(MessageChannels.direct("dataInLogRoomChannel"))
                .transform(armoredRoom -> {
                    try {
                        return objectMapper.writeValueAsString(armoredRoom);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .handle(Files.outboundAdapter(new File("/home/another/dev/developering/stay-go"))
                        .fileExistsMode(FileExistsMode.APPEND)
                        .appendNewLine(true))
                .get();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Поддержка Java 8 Time API (если используется)
        return mapper;
    }
}
