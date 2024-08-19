package com.project.studyenglish.controller;


import com.project.studyenglish.dto.GrammarDto;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.dto.request.GrammarRequest;
import com.project.studyenglish.dto.request.ProductRequest;
import com.project.studyenglish.models.GrammarEntity;
import com.project.studyenglish.service.impl.GrammarService;
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
@RequestMapping(value = "api/grammar")
public class GrammarController {
    @Autowired
    private GrammarService grammarService;

    @GetMapping(value = "/by-category/{id}")
    public ResponseEntity<List<GrammarDto>> getAllGrammarByCategoryId(@PathVariable Long id) {
        List<GrammarDto> grammarDtoList = grammarService.getAllGrammarByCategory(id);
        return ResponseEntity.ok(grammarDtoList);
    }
    @PostMapping("")
    public ResponseEntity<?> createGrammar(@Valid @RequestBody GrammarRequest grammarRequest, BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessages);
            }
            grammarService.createGrammar(grammarRequest);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Insert grammar successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGrammar(
            @PathVariable long id,
            @RequestBody GrammarRequest grammarRequest) {
        try {
            grammarService.updateGrammar(grammarRequest);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Update grammar successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getGrammarById(@PathVariable Long id) {
        try{
            GrammarDto grammarDto = grammarService.getGrammarById(id);
            return ResponseEntity.ok(grammarDto);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi xảy ra");
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGrammarById(@PathVariable Long id) {
        grammarService.deleteGrammar(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete product with id: "+id+" successfully");
        return ResponseEntity.ok(response);
    }
}
