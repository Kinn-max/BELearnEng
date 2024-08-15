package com.project.studyenglish.service.impl;

import com.project.studyenglish.dto.GrammarDto;
import com.project.studyenglish.models.GrammarEntity;
import com.project.studyenglish.repository.GrammarRepository;
import com.project.studyenglish.service.IGrammarService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GrammarService implements IGrammarService {
    @Autowired
    private GrammarRepository grammarRepository;
    @Autowired
    private ModelMapper modelMapper;
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
}
