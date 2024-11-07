package com.project.studyenglish.controller;

import com.project.studyenglish.dto.ExamDto;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.dto.request.ExamRequest;
import com.project.studyenglish.service.impl.ExamService;
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
@RequestMapping(value = "api/exam")
public class ExamController {
    @Autowired
    private ExamService examService;
    @GetMapping(value =  "")
    public ResponseEntity<List<ExamDto>> getAllExam() {
       List<ExamDto> examDtoList = examService.getAllExam();
       return ResponseEntity.ok(examDtoList);
    }
    @GetMapping(value = "/by-category/{id}")
    public ResponseEntity<List<ExamDto>> getAllExamByCategoryId(@PathVariable Long id ,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        List<ExamDto> examDtoList = examService.getAllExamByCategory(id,size,page);
        return ResponseEntity.ok(examDtoList);
    }
    @PostMapping("")
    public ResponseEntity<?> createExam(@Valid @RequestBody ExamRequest examRequest, BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessages);
            }
            examService.createExam(examRequest);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Insert question successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExam(
            @PathVariable long id,
            @RequestBody ExamRequest examRequest) {
        try {
            examService.updateExam(examRequest);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Update question successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete question successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/by-category/{id}/status")
    public ResponseEntity<?> getAllExamByCategoryStatus(@PathVariable Long id) {
        try {
            List<ExamDto> result = examService.getAllExamByCategoryAndStatus(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
