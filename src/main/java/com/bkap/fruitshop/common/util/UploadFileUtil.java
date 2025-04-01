package com.bkap.fruitshop.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
@Slf4j
public class UploadFileUtil {

    private final Path uploadPath = Paths.get("src/main/resources/uploads");

    public String saveImage(MultipartFile imageFile) {
        String fileName = imageFile.getOriginalFilename();

        try{
            if (!Files.exists(uploadPath)) {
                Files.createDirectory(uploadPath);
            }
            assert fileName != null;
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }catch (IOException e){
            throw new RuntimeException("Fail to store file", e);
        }

    }

    public void deleteImage(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            log.warn("Invalid file name provided for deletion");
            throw new IllegalArgumentException("File name is null or empty");
        }
        try {
            Path filePath = uploadPath.resolve(fileName);
            if (!Files.exists(filePath)) {
                log.warn("Image file not found: {}", fileName);
                return;
            }
            Files.delete(filePath);
            log.info("Successfully deleted image: {}", fileName);
        }catch (IOException e){
            log.error("Fail to delete image: {}", fileName, e);
            throw new RuntimeException("Fail to delete image", e);
        }
    }
}
