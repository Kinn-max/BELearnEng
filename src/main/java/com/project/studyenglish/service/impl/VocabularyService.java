package com.project.studyenglish.service.impl;

import com.project.studyenglish.converter.VocabularyConverter;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.models.VocabularyEntity;
import com.project.studyenglish.repository.VocabularyRepository;
import com.project.studyenglish.service.IVocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VocabularyService implements IVocabularyService {
    @Autowired
    private VocabularyRepository vocabularyRepository;
    @Autowired
    private VocabularyConverter vocabularyConverter;
    @Override
    public List<VocabularyDto> getAllVocabularies() {
        List<VocabularyEntity> vocabularyEntityList = vocabularyRepository.findAll();
        List<VocabularyDto> vocabularyDtoList = new ArrayList<>();
        for (VocabularyEntity vocabularyEntity : vocabularyEntityList) {
            VocabularyDto vocabularyDto = vocabularyConverter.toVocabularyDto(vocabularyEntity);
            vocabularyDtoList.add(vocabularyDto);
        }
        return vocabularyDtoList;
    }

    @Override
    public List<VocabularyDto> getAllVocabularyByCategory(Long id) {
       List<VocabularyEntity> vocabularyEntityList = vocabularyRepository.findByCategoryEntity_Id(id);
       List<VocabularyDto> vocabularyDtoList = new ArrayList<>();
        for (VocabularyEntity vocabularyEntity : vocabularyEntityList) {
            VocabularyDto vocabularyDto = vocabularyConverter.toVocabularyDto(vocabularyEntity);
            vocabularyDtoList.add(vocabularyDto);
        }
        return vocabularyDtoList;
    }
}
