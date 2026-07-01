package com.smartvault.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @org.springframework.beans.factory.annotation.Value("${app.cloudinary.api-key:}")
    private String apiKey;

    @org.springframework.beans.factory.annotation.Value("${app.cloudinary.api-secret:}")
    private String apiSecret;

    public Map uploadFile(MultipartFile file) throws IOException {
        if (apiKey == null || apiKey.trim().isEmpty() || apiSecret == null || apiSecret.trim().isEmpty() || "your_api_key".equals(apiKey)) {
            log.info("Cloudinary credentials are not configured or using placeholders. Falling back to local file storage.");
            java.io.File uploadDir = new java.io.File("uploads");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String uniqueName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
            java.io.File destination = new java.io.File(uploadDir, uniqueName);
            file.transferTo(destination);

            java.util.Map<String, Object> localResult = new java.util.HashMap<>();
            localResult.put("secure_url", "http://localhost:8080/api/documents/files/" + uniqueName);
            localResult.put("public_id", "local_" + uniqueName);
            return localResult;
        }

        try {
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        } catch (IOException e) {
            log.error("Error uploading file to Cloudinary", e);
            throw e;
        }
    }

    public void deleteFile(String publicId) {
        if (publicId != null && publicId.startsWith("local_")) {
            String fileName = publicId.substring(6);
            java.io.File file = new java.io.File("uploads", fileName);
            if (file.exists()) {
                file.delete();
                log.info("Local file deleted: {}", fileName);
            }
            return;
        }

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            log.error("Error deleting file from Cloudinary: {}", publicId, e);
        }
    }
}
