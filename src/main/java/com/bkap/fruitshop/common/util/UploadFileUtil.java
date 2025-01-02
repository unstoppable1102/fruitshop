package com.bkap.fruitshop.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class UploadFileUtil {

    public String saveImage(MultipartFile imageFile) {
        String fileName = imageFile.getOriginalFilename();

        try{
            Path uploadPath = Paths.get("src/main/resources/uploads");
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
}
