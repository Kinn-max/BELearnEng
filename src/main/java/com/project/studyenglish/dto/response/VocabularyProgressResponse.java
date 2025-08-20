package com.project.studyenglish.dto.response;

import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyProgressResponse {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String level;
    private String categoryName;
    private String masteryLevel;
    private Integer correctCount;
    private Integer incorrectCount;
    private Integer totalAttempts;
    private Double successRate;
    private String updateDate;

}
