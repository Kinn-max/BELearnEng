package com.project.studyenglish.converter;

import com.project.studyenglish.dto.ExamDto;
import com.project.studyenglish.models.ExamEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExamConverter {
    @Autowired
    private ModelMapper modelMapper;

    public ExamDto toExam(ExamEntity examEntity) {
        ExamDto examDto = modelMapper.map(examEntity, ExamDto.class);
        return examDto;
    }
}
