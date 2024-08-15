package com.project.studyenglish.service.impl;

import com.project.studyenglish.converter.ExamConverter;
import com.project.studyenglish.dto.ExamDto;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.models.ExamEntity;
import com.project.studyenglish.repository.ExamRepository;
import com.project.studyenglish.service.IExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamService implements IExamService {
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ExamConverter examConverter;
    @Override
    public List<ExamDto> getAllExam() {
        List<ExamEntity>  examEntityList = examRepository.findAll();
        List<ExamDto> examDtoList = new ArrayList<>();
        for (ExamEntity examEntity : examEntityList) {
            ExamDto examDto = examConverter.toExam(examEntity);
            examDtoList.add(examDto);
        }
        return examDtoList;
    }

    @Override
    public List<ExamDto> getAllExamByCategory(Long id) {
        List<ExamEntity> examEntityList = examRepository.findByCategoryEntity_Id(id);
        List<ExamDto> examDtoList = new ArrayList<>();
        for (ExamEntity examEntity : examEntityList) {
            ExamDto examDto = examConverter.toExam(examEntity);
            examDtoList.add(examDto);
        }
        return examDtoList;
    }
}
