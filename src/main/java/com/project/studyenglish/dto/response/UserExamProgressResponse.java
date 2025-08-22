package com.project.studyenglish.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserExamProgressResponse {
    private Integer currentLevel;
    private Integer maxLevelUnlocked;
    private Integer totalLevels;
    private List<ExamLevelResponse> levels;
}