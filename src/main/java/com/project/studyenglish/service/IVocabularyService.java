package com.project.studyenglish.service;

import com.project.studyenglish.dto.VocabularyDto;

import java.util.List;

public interface IVocabularyService {
    List<VocabularyDto> getAllVocabularies();
    List<VocabularyDto> getAllVocabularyByCategory(Long id);
}
