package com.project.studyenglish.service;


import com.project.studyenglish.dto.ExamDto;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.dto.request.ExamRequest;
import com.project.studyenglish.dto.request.ProductRequest;
import com.project.studyenglish.models.ExamEntity;
import com.project.studyenglish.models.ProductEntity;

import java.util.List;

public interface IExamService {
    List<ExamDto> getAllExam();
    List<ExamDto> getAllExamByCategory(Long id);
    List<ExamDto> getAllExamByCategoryAndStatus(Long id);
    ExamEntity createExam(ExamRequest examRequest);
    ExamEntity updateExam(ExamRequest examRequest);
    ExamEntity getExamById(Long id);
    void deleteExam(Long id);
}
