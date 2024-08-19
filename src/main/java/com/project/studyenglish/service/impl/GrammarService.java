package com.project.studyenglish.service.impl;

import com.project.studyenglish.dto.GrammarDto;
import com.project.studyenglish.dto.request.GrammarRequest;
import com.project.studyenglish.models.CategoryEntity;
import com.project.studyenglish.models.GrammarEntity;
import com.project.studyenglish.models.ProductEntity;
import com.project.studyenglish.repository.CategoryRepository;
import com.project.studyenglish.repository.GrammarRepository;
import com.project.studyenglish.service.IGrammarService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GrammarService implements IGrammarService {
    @Autowired
    private GrammarRepository grammarRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<GrammarDto> getAllGrammarByCategory(Long id) {
        List<GrammarEntity>  grammarEntityList =  grammarRepository.findByCategoryEntity_Id(id);
        List<GrammarDto> grammarDtoList = new ArrayList<>();
        for (GrammarEntity grammarEntity : grammarEntityList) {
            GrammarDto grammarDto = modelMapper.map(grammarEntity, GrammarDto.class);
            grammarDtoList.add(grammarDto);
        }
        return grammarDtoList;
    }

    @Override
    public GrammarEntity createGrammar(GrammarRequest grammarRequest) {
        CategoryEntity existingCategory = categoryRepository.findById(grammarRequest.getCategoryId())
                .orElseThrow(() -> new DataIntegrityViolationException("Category not found"));
        GrammarEntity grammarEntity = GrammarEntity.builder()
                .categoryEntity(existingCategory)
                .name(grammarRequest.getName())
                .content(grammarRequest.getContent())
                .image(grammarRequest.getImage())
                .build();
        return grammarRepository.save(grammarEntity);
    }

    @Override
    public GrammarEntity updateGrammar(GrammarRequest grammarRequest) {
        GrammarEntity grammarEntity = getGrammarEntityById(grammarRequest.getId());
        if (grammarEntity != null) {
            grammarEntity.setName(grammarRequest.getName());
            grammarEntity.setContent(grammarRequest.getContent());
            if (grammarRequest.getImage() != null) {
                grammarEntity.setImage(grammarRequest.getImage());
            }
            return grammarRepository.save(grammarEntity);
        }
        return null;
    }

    @Override
    public GrammarDto getGrammarById(Long id) {
        GrammarEntity grammarEntity = grammarRepository.findById(id).orElseThrow(() -> new DataIntegrityViolationException("Grammar not found"));
        GrammarDto grammarDto = modelMapper.map(grammarEntity, GrammarDto.class);
        return grammarDto;
    }

    @Override
    public GrammarEntity getGrammarEntityById(Long id) {
        return grammarRepository.findById(id).orElseThrow(
                () -> new DataIntegrityViolationException("Grammar not found"));
    }

    @Override
    public void deleteGrammar(Long id) {
        grammarRepository.deleteById(id);
    }
}
