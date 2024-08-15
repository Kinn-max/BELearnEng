package com.project.studyenglish.controller;


import com.project.studyenglish.dto.GrammarDto;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.models.GrammarEntity;
import com.project.studyenglish.service.impl.GrammarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
