package com.staygo.file_writer;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "coordinateCityAndCountryChannel")
public interface FileWriterGateway {
    void writeToFileCoordinate(@Header(FileHeaders.FILENAME) String filename, Object data);
}
