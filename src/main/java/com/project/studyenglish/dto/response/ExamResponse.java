package com.project.studyenglish.dto.response;


import com.project.studyenglish.enums.DifficultyLevel;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponse {
    private Long id;
    private String question;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String image;
    private Integer level;
    private DifficultyLevel difficulty;
    private Long categoryId;
}