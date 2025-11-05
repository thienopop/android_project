package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.access-url}")
    private String accessUrl;

    @Value("${file.allowed-extensions}")
    private String allowedExtensions;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.status(400).body("Tệp tin tải lên không được để trống");
        }

        Path uploadDirectory = Paths.get(uploadDir);
        Files.createDirectories(uploadDirectory);

        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.trim().isEmpty()) {
            return ResponseEntity.status(400).body("Không tìm thấy tên tệp tin trong yêu cầu tải lên");
        }

        String extension = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalName.substring(dotIndex);
            originalName = originalName.substring(0, dotIndex);
        }

        List<String> allowedList = Arrays.asList(allowedExtensions.split(","));
        if (!allowedList.contains(extension)) {
            return ResponseEntity.status(400).body("Định dạng tệp không được phép: " + extension);
        }

        String uniqueName = originalName + "_" + System.currentTimeMillis() + extension;
        Path filePath = uploadDirectory.resolve(uniqueName);
        file.transferTo(filePath);

        String fileAccessUrl = accessUrl + "/" + uniqueName;

        Map<String, Object> response = Map.of(
                "message", "Tải lên tệp tin thành công",
                "fileName", uniqueName,
                "fileUrl", fileAccessUrl
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename) throws IOException {
        Path uploadDirectory = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = uploadDirectory.resolve(filename).normalize();
        if (!filePath.startsWith(uploadDirectory)) {
            return ResponseEntity.status(400).body("Đường dẫn tệp không hợp lệ.");
        }

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.status(404).body("Không tìm thấy hoặc không thể đọc tệp tin: " + filename);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
