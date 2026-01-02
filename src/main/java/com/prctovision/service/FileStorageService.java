package com.prctovision.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("proctor_evidence");

    public FileStorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String saveFile(MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path destination = rootLocation.resolve(filename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return destination.toString();
    }
}
