package com.project.studyenglish.service;

import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.dto.request.VocabularyRequest;
import com.project.studyenglish.models.VocabularyEntity;

import java.util.List;

public interface IVocabularyService {
    List<VocabularyDto> getAllVocabularies();
    List<VocabularyDto> getAllVocabularyByCategory(Long id);
    VocabularyEntity createVocabulary(VocabularyRequest vocabularyRequest);
    void deleteVocabulary(Long id);
    VocabularyDto getVocabularyById(Long id);
}
