package com.project.studyenglish.controller;

import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.dto.request.UserSavedVocabularyListRequest;

import com.project.studyenglish.dto.request.VocabularyLearningProgressRequest;
import com.project.studyenglish.dto.request.VocabularyRequest;
import com.project.studyenglish.dto.response.VocabularyIncorrectResponse;
import com.project.studyenglish.dto.response.VocabularyProgressOverviewResponse;
import com.project.studyenglish.dto.response.VocabularyProgressResponse;
import com.project.studyenglish.service.IVocabularyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Transactional
@RequestMapping(value = "api/vocabulary")
public class VocabularyController {
    @Autowired
    private IVocabularyService vocabularyService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @GetMapping(value = "")
    public ResponseEntity<List<VocabularyDto>> getAllVocabulary() {
        List<VocabularyDto> vocabularyDtoList = vocabularyService.getAllVocabularies();
        return ResponseEntity.ok(vocabularyDtoList);
    }
    @GetMapping(value = "/by-category/{id}")
    public ResponseEntity<List<VocabularyDto>> getAllVocabularyByCategoryId(@PathVariable Long id) {
        List<VocabularyDto> vocabularyDtoList = vocabularyService.getAllVocabularyByCategory(id);
        return ResponseEntity.ok(vocabularyDtoList);
    }
    @PostMapping("")
    public ResponseEntity<?> createVocabulary(@Valid @RequestBody VocabularyRequest vocabularyRequest, BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }
        vocabularyService.createVocabulary(vocabularyRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Insert vocabulary successfully");
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVocabulary(@PathVariable Long id) {
        vocabularyService.deleteVocabulary(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete vocabulary with id: "+id+" successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getVocabularyById(@PathVariable Long id) {
        try{
            VocabularyDto vocabularyDto = vocabularyService.getVocabularyById(id);
            return ResponseEntity.ok(vocabularyDto);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi xảy ra");
        }

    }
    @GetMapping("/by-category/{id}/status")
    public ResponseEntity<?> getAllVocabularyByCategoryStatus(@PathVariable Long id, HttpServletRequest request) {

        try {
            String token = request.getHeader("Authorization").substring(7);
            Long userId = jwtTokenUtil.extractUserId(token);
            List<VocabularyDto> result= vocabularyService.getAllVocabularyByCategoryAndStatus(id,userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/result/progress")
    public ResponseEntity<?> saveProgress(@RequestBody VocabularyLearningProgressRequest rq) {
        try {
            vocabularyService.saveOrUpdateProgress(rq);
            return ResponseEntity.ok(Map.of("message","success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/result/progress/overview/{userId}/{cateId}")
    public ResponseEntity<?> overviewProgress(@PathVariable Long userId, @PathVariable Long cateId) {
        try {
            VocabularyProgressOverviewResponse vocab = vocabularyService.getOverview(userId, cateId);

            if (vocab == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No progress found for this user and category");
            }
            return ResponseEntity.ok(vocab);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/result/progress/{userId}/{cateId}")
    public ResponseEntity<?> getProgress(@PathVariable Long userId, @PathVariable Long cateId) {
        try {
            VocabularyProgressResponse vocab = vocabularyService.getProgress(userId, cateId);

            if (vocab == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No progress found for this user and category");
            }
            return ResponseEntity.ok(vocab);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/incorrect/by-progress/{id}")
    public ResponseEntity<?> getVocabIncorrectByProgressId(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            Long userId = jwtTokenUtil.extractUserId(token);
            VocabularyIncorrectResponse vocab = vocabularyService.getVocabularyIncorrectByProgressId(id,userId);
            return ResponseEntity.ok(vocab);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/favorite")
    public ResponseEntity<?> saveVocabFavorite(@RequestBody UserSavedVocabularyListRequest rq) {
        try {
            vocabularyService.saveUserSavedVocabularies(rq);
            return ResponseEntity.ok(Map.of("message","success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
