package com.project.studyenglish.dto.request;

import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VocabularyLearningProgressRequest {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String masteryLevel;
    private Integer correctCount;
    private Integer incorrectCount;
    private Integer totalAttempts;
    private Double successRate;
    private List<VocabularyIncorrectRequest> incorrectAnswers;
}

