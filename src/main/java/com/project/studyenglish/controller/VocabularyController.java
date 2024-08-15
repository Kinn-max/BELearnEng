package com.project.studyenglish.controller;

import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.service.IVocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
