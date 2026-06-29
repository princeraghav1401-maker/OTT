package com.streamverse.backend.service.impl;

import com.streamverse.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalFileStorageServiceImpl implements FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public String storeFile(MultipartFile file, String folderName) {

        try {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            String originalFileName = file.getOriginalFilename();

            if (originalFileName == null || originalFileName.isBlank()) {
                throw new RuntimeException("Invalid file name");
            }

            String fileExtension = "";

            int dotIndex = originalFileName.lastIndexOf(".");
            if (dotIndex >= 0) {
                fileExtension = originalFileName.substring(dotIndex);
            }

            String newFileName = UUID.randomUUID() + fileExtension;

            Path folderPath = Paths.get(uploadDir, folderName)
                    .toAbsolutePath()
                    .normalize();

            Files.createDirectories(folderPath);

            Path targetPath = folderPath.resolve(newFileName);

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "/uploads/" + folderName + "/" + newFileName;

        } catch (IOException e) {
            throw new RuntimeException("Could not store file: " + e.getMessage());
        }
    }
}