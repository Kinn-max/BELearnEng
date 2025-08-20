package com.project.studyenglish.dto.response;

import lombok.*;

import java.sql.Date;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyProgressOverviewResponse {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private String masteryLevel;
    private Double successRate;
    private String updateDate;
}
