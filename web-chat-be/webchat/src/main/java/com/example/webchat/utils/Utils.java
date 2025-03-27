package com.example.webchat.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Component
public class Utils {
    public String storeFile(MultipartFile file,String fileNameOld)throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("invalid file format");
        }
        String uploadDirExist = "uploads/";
        // Xóa ảnh cũ nếu có
        if (fileNameOld != null && !fileNameOld.isEmpty()) {
            Path oldFilePath = Paths.get(uploadDirExist + fileNameOld);
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);  // Xóa file ảnh cũ
            }
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFileName = UUID.randomUUID().toString() + "-" + fileName;
        Path uploadDir = Paths.get("uploads/");
        if (!Files.exists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }



    public static boolean isImageFile(MultipartFile file) {
        if (file == null || file.getContentType() == null) {
            return false;
        }
        String contentType = file.getContentType().toLowerCase();
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/jpg");
    }
}
