package com.project.studyenglish.service;


import com.project.studyenglish.dto.ExamDto;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.models.ExamEntity;

import java.util.List;

public interface IExamService {
    List<ExamDto> getAllExam();
    List<ExamDto> getAllExamByCategory(Long id);
}
