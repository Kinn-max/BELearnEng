package com.project.studyenglish.service;

import com.project.studyenglish.dto.GrammarDto;
import com.project.studyenglish.dto.request.GrammarRequest;
import com.project.studyenglish.models.GrammarEntity;

import java.util.List;

public interface IGrammarService {
    List<GrammarDto> getAllGrammarByCategory(Long id);
    GrammarEntity createGrammar(GrammarRequest grammarRequest);
    GrammarEntity updateGrammar(GrammarRequest grammarRequest);
    GrammarDto getGrammarById(Long id);
    GrammarEntity getGrammarEntityById(Long id);
    void deleteGrammar(Long id);
    List<GrammarDto> getAllGrammarRandom();
}
