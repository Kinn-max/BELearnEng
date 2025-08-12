package com.project.studyenglish.dto.sound;

public record Base64Audio(
        String mimeType,
        String data  // The Base64 encoded audio string
) {}