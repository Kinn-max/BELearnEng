package com.project.studyenglish.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubmissionResponse {
    private Long attemptId;
    private Double scorePercentage;
    private Integer correctAnswers;
    private Integer totalQuestions;
    private Boolean isPassed;
    private Boolean levelUnlocked;
    private Integer newMaxLevel;
    private String message;
    private List<ExamResultResponse> questionResults;
}