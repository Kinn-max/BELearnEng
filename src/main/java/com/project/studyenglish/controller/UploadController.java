package com.project.studyenglish.controller;

import com.project.studyenglish.service.impl.GoogleCloudStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/upload")
public class UploadController {
    private final GoogleCloudStorageService storageService;

    public UploadController(GoogleCloudStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String url = storageService.uploadFile(file);
        return ResponseEntity.ok(url);
    }
}
