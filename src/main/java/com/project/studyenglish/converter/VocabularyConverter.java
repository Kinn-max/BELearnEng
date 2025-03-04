package com.project.studyenglish.converter;

import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.dto.request.VocabularyRequest;
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
    public VocabularyEntity toVocabularyEntity(VocabularyRequest vocabularyRequest) {
        VocabularyEntity vocabularyEntity = new VocabularyEntity();
        vocabularyEntity.setId(vocabularyRequest.getId());
        vocabularyEntity.setName(vocabularyRequest.getName());
        vocabularyEntity.setNameVietnamese(vocabularyRequest.getNameVietnamese());
        vocabularyEntity.setTranscription(vocabularyRequest.getTranscription());
        vocabularyEntity.setPart(vocabularyRequest.getPart());
        vocabularyEntity.setDescription(vocabularyRequest.getDescription());
        vocabularyEntity.setImage(vocabularyRequest.getImage());

        return vocabularyEntity;
    }
}
