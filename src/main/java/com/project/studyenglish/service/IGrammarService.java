package com.project.studyenglish.service;

import com.project.studyenglish.dto.GrammarDto;

import java.util.List;

public interface IGrammarService {
    List<GrammarDto> getAllGrammarByCategory(Long id);
}
