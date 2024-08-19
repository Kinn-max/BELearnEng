package com.project.studyenglish.service.impl;

import com.project.studyenglish.converter.VocabularyConverter;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.dto.request.VocabularyRequest;
import com.project.studyenglish.models.CategoryEntity;
import com.project.studyenglish.models.ProductEntity;
import com.project.studyenglish.models.VocabularyEntity;
import com.project.studyenglish.repository.CategoryRepository;
import com.project.studyenglish.repository.VocabularyRepository;
import com.project.studyenglish.service.IVocabularyService;
import org.modelmapper.ModelMapper;
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
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
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

    @Override
    public VocabularyEntity createVocabulary(VocabularyRequest vocabularyRequest) {
        CategoryEntity categoryEntity = categoryRepository.findById(vocabularyRequest.getIdTopic()).get();
            VocabularyEntity vocabularyEntity = modelMapper.map(vocabularyRequest, VocabularyEntity.class);
        if (vocabularyRequest.getImage() != null && !vocabularyRequest.getImage().isEmpty()) {
            vocabularyEntity.setImage(vocabularyRequest.getImage());
        }
        if(vocabularyRequest.getSound() != null && !vocabularyRequest.getSound().isEmpty()) {
            vocabularyEntity.setSound(vocabularyRequest.getSound());
        }
        vocabularyEntity.setCategoryEntity(categoryEntity);
        return vocabularyRepository.save(vocabularyEntity);
    }

    @Override
    public void deleteVocabulary(Long id) {
        vocabularyRepository.deleteById(id);
    }

    @Override
    public VocabularyDto getVocabularyById(Long id) {
        VocabularyEntity vocabularyEntity = vocabularyRepository.findById(id).get();
        VocabularyDto vocabularyDto = modelMapper.map(vocabularyEntity, VocabularyDto.class);
        return vocabularyDto;
    }
}
