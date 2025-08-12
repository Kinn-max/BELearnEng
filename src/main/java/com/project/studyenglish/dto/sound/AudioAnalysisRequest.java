package com.project.studyenglish.dto.sound;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public record AudioAnalysisRequest(
        MultipartFile audioFile,
        String message
) {}
