package com.project.studyenglish.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamRequest {
    private Long id;
    @NotNull(message = "ID Category error")
    private Long categoryId;

    @NotBlank(message = "Question is Required")
    private String question;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    @NotBlank(message = "Answer is required")
    private String answer;
    private String image;
}
