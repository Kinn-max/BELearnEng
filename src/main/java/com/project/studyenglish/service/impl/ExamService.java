package com.project.studyenglish.service.impl;

import com.project.studyenglish.converter.ExamConverter;
import com.project.studyenglish.customexceptions.DataNotFoundException;
import com.project.studyenglish.dto.ExamDto;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.dto.request.ExamRequest;
import com.project.studyenglish.models.CategoryEntity;
import com.project.studyenglish.models.ExamEntity;
import com.project.studyenglish.models.VocabularyEntity;
import com.project.studyenglish.repository.CategoryRepository;
import com.project.studyenglish.repository.ExamRepository;
import com.project.studyenglish.service.IExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamService implements IExamService {
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ExamConverter examConverter;
    @Autowired
    private CategoryRepository categoryRepository;

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

    @Override
    public List<ExamDto> getAllExamByCategoryAndStatus(Long id) {
        List<ExamEntity> examEntityList = examRepository.findByCategoryEntity_Id(id);
        List<ExamDto> examDtoList = new ArrayList<>();
        for (ExamEntity examEntity : examEntityList) {
            if(examEntity.getCategoryEntity().getStatus() == true){
                ExamDto examDto = examConverter.toExam(examEntity);
                examDtoList.add(examDto);
            }
        }
        return examDtoList;
    }

    @Override
    public ExamEntity createExam(ExamRequest examRequest) throws DataNotFoundException {
        CategoryEntity existingCategory = categoryRepository.findById(examRequest.getCategoryId())
                .orElseThrow(() -> new DataIntegrityViolationException("Category not found"));
        ExamEntity examEntity = ExamEntity.builder()
                .question(examRequest.getQuestion())
                .answer(examRequest.getAnswer())
                .answerA(examRequest.getAnswerA())
                .answerB(examRequest.getAnswerB())
                .answerC(examRequest.getAnswerC())
                .answerD(examRequest.getAnswerD())
                .image(examRequest.getImage())
                .categoryEntity(existingCategory)
                .build();
        return examRepository.save(examEntity);
    }

    @Override
    public ExamEntity updateExam(ExamRequest examRequest) {
        ExamEntity existingExam = getExamById(examRequest.getId());
        if (existingExam != null) {
            CategoryEntity existingCategory = categoryRepository.findById(examRequest.getCategoryId())
                    .orElseThrow(() -> new DataIntegrityViolationException("Category not found"));
            existingExam.setQuestion(examRequest.getQuestion());
            existingExam.setAnswer(examRequest.getAnswer());
            existingExam.setAnswerA(examRequest.getAnswerA());
            existingExam.setAnswerB(examRequest.getAnswerB());
            existingExam.setAnswerC(examRequest.getAnswerC());
            existingExam.setAnswerD(examRequest.getAnswerD());
            if (examRequest.getImage() != null) {
                existingExam.setImage(examRequest.getImage());
            }
            existingExam.setCategoryEntity(existingCategory);
            return examRepository.save(existingExam);
        }
        return null;
    }

    @Override
    public ExamEntity getExamById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Exam not found"));
    }

    @Override
    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }
}
