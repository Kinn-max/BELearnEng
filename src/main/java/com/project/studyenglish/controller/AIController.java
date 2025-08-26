package com.project.studyenglish.controller;

import com.project.studyenglish.dto.request.AIRenderQuestion;
import com.project.studyenglish.dto.request.AITranslateRequest;
import com.project.studyenglish.service.impl.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;
    @PostMapping("/translate")
    public ResponseEntity<?> getTranslate(@RequestBody AITranslateRequest translateRequest ) {
        return ResponseEntity.ok(aiService.translate(translateRequest));
    }
    @PostMapping("/render-question")
    public ResponseEntity<?> getRenderQuestion(@RequestBody AIRenderQuestion rq ) {
        return ResponseEntity.ok(aiService.getRenderQuestion(rq));
    }

}