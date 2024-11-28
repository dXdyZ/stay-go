package com.staygo.file_writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staygo.enity.weather.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.messaging.MessageChannel;

import java.io.File;
import java.io.IOException;


@Slf4j
@Configuration
@IntegrationComponentScan
public class FileWriterCoordinateConfig {

    @Bean
    public IntegrationFlow fileWriterFlow(ObjectMapper objectMapper) {
        return IntegrationFlow
                .from(MessageChannels.direct("coordinateCityAndCountryChannel"))
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
    public MessageChannel fileInputChannel() {
        return new DirectChannel();
    }


    @Bean
    public MessageChannel processFileChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow fileReadingFlow() {
        return IntegrationFlow
                .from(Files.inboundAdapter(new File("/home/another/dev/developering/stay-go"))
                                .patternFilter("*.txt"),
                        e -> e.poller(Pollers.fixedDelay(1000)))
                .transform(Files.toStringTransformer())
                .channel("processFileChannel")
                .get();
    }

    @Bean
    public IntegrationFlow processFileFlow(ObjectMapper objectMapper) {
        return IntegrationFlow
                .from("processFileChannel")
                .handle(message -> {
                    String data = String.valueOf(message.getPayload());
                    try {
                        Country country = objectMapper.readValue(data, Country.class);
                        log.info("resulting mapping: {}", country);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .get();
    }


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}









