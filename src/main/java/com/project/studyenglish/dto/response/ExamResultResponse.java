package com.project.studyenglish.dto.response;
import com.project.studyenglish.enums.DifficultyLevel;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultResponse {
    private Long id;
    private String question;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String correctAnswer;
    private String userAnswer;
    private Boolean isCorrect;
    private String image;
    private Integer level;
    private DifficultyLevel difficulty;
    private String explanation;
}