package com.project.studyenglish.controller;

import com.project.studyenglish.dto.CategoryOfCommonDto;
import com.project.studyenglish.dto.ProductDto;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.dto.request.CategoryRequest;
import com.project.studyenglish.dto.request.VocabularyRequest;
import com.project.studyenglish.service.IVocabularyService;
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
    public ResponseEntity<?> getAllVocabularyByCategoryStatus(@PathVariable Long id) {
        try {
            List<VocabularyDto> result= vocabularyService.getAllVocabularyByCategoryAndStatus(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
