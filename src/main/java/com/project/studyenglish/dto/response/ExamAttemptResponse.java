package com.project.studyenglish.dto.response;

import lombok.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamAttemptResponse {
    private Long id;
    private Integer level;
    private Double scorePercentage;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Boolean isPassed;
    private Date attemptDate;
    private Integer timeTaken;
}