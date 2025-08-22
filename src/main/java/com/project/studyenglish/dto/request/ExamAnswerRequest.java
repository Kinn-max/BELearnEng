package com.project.studyenglish.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamAnswerRequest {
    private Long questionId;
    private String selectedAnswer;
}