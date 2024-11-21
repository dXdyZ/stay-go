package com.example.sendermessagestaygo.itegration;

import com.example.sendermessagestaygo.enity.ArmoredRoomDTO;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "dataInLogRoomChannel")
public interface FileWriterGateway {
    void writeToFile(@Header(FileHeaders.FILENAME) String filename, Object data);
}
