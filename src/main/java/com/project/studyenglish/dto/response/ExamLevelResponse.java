package com.project.studyenglish.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamLevelResponse {
    private Long categoryId;
    private String categoryName;
    private String categoryImage;
    private Integer level;
    private Boolean isUnlocked;
    private Boolean isPassed;
    private Integer totalQuestions;
    private Double bestScore;
    private List<ExamAttemptResponse> attempts;
}