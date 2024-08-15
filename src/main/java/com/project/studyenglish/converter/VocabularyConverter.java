package com.project.studyenglish.converter;

import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.models.VocabularyEntity;
import org.antlr.v4.runtime.Vocabulary;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VocabularyConverter {
    @Autowired
    private ModelMapper modelMapper;
    public VocabularyDto toVocabularyDto(VocabularyEntity vocabulary) {
        VocabularyDto vocabularyDto = modelMapper.map(vocabulary, VocabularyDto.class);
        return vocabularyDto;
    }
}
