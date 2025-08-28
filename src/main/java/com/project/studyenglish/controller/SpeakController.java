package com.project.studyenglish.controller;

import com.project.studyenglish.service.impl.SpeakService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/speak")
@RequiredArgsConstructor
public class SpeakController {

    private  final SpeakService speakService;

    @GetMapping("/home")
    public ResponseEntity<?> getSpeakHome(HttpServletRequest request) {
        return ResponseEntity.ok(speakService.getAllSpeakCategory(request));
    }
    @GetMapping("/by-category/{cateId}")
    public ResponseEntity<?> getALlSpeakDataByCategoryId(@PathVariable Long cateId) {
        return  ResponseEntity.ok(speakService.getAllSpeakDataByCateId(cateId));
    }
}
