package com.lfey.statygo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.store.path}")
    private String mainPath;

    public String store(MultipartFile multipartFile) {
        try {
            String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
            Path uploadPath = Paths.get(mainPath).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(fileName);

            Files.createDirectories(uploadPath);

            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
