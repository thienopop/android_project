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
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Path uploadDirectory = Paths.get(uploadDir);
        Files.createDirectories(uploadDirectory);

        String originalName = file.getOriginalFilename();
        String extension = "";

        List<String> allowedList = Arrays.asList(allowedExtensions.split(","));
        if (!allowedList.contains(extension)) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Định dạng tệp không được phép: " + extension));
        }

        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalName.substring(dotIndex);
            originalName = originalName.substring(0, dotIndex);
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
        Path filePath = Paths.get(uploadDir).resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.status(404).body("Không tìm thấy hoặc không thể đọc tệp tin: " + filename);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
