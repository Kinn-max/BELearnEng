package com.project.studyenglish.controller;

import com.project.studyenglish.dto.ExamDto;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.service.impl.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional
@RequestMapping(value = "api/exam")
public class ExamController {
    @Autowired
    private ExamService examService;
    @GetMapping(value =  "")
    public ResponseEntity<List<ExamDto>> getAllExam() {
       List<ExamDto> examDtoList = examService.getAllExam();
       return ResponseEntity.ok(examDtoList);
    }
    @GetMapping(value = "/by-category/{id}")
    public ResponseEntity<List<ExamDto>> getAllExamByCategoryId(@PathVariable Long id) {
        List<ExamDto> examDtoList = examService.getAllExamByCategory(id);
        return ResponseEntity.ok(examDtoList);
    }

}
