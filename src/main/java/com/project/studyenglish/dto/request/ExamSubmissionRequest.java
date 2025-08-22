package com.project.studyenglish.dto.request;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubmissionRequest {
    private Long userId;
    private Long categoryId;
    private Integer level;
    private List<ExamAnswerRequest> answers;
    private Integer timeTaken;
}