package com.project.studyenglish.controller;


import com.project.studyenglish.dto.sound.AudioAnalysisRequest;
import com.project.studyenglish.dto.sound.AudioAnalysisResponse;
import com.project.studyenglish.service.impl.AudioAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/audio")
@RequiredArgsConstructor
public class AudioAnalysisController {

    private final AudioAnalysisService audioAnalysisService;

    @PostMapping("/analyze")
    public ResponseEntity<AudioAnalysisResponse> analyzeAudio(
            @RequestParam("audioFile") MultipartFile audioFile,
            @RequestParam("message") String expectedText) {

        try {
            // Validate input
            if (audioFile.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new AudioAnalysisResponse("Audio file is required", null)
                );
            }

            if (expectedText == null || expectedText.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new AudioAnalysisResponse("Expected text is required", null)
                );
            }

            // Process the request
            AudioAnalysisRequest request = new AudioAnalysisRequest(audioFile, expectedText.trim());
            AudioAnalysisResponse response = audioAnalysisService.audioAnalysis(request);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new AudioAnalysisResponse("Internal server error: " + e.getMessage(), null)
            );
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Audio Analysis Service is running");
    }

    @PostMapping("/test-tts")
    public ResponseEntity<AudioAnalysisResponse> testTTS(@RequestParam("text") String text) {
        try {
            // Test chỉ phần TTS
            AudioAnalysisRequest request = new AudioAnalysisRequest(null, text);
            AudioAnalysisResponse response = audioAnalysisService.audioAnalysis(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new AudioAnalysisResponse("TTS test failed: " + e.getMessage(), null)
            );
        }
    }
}