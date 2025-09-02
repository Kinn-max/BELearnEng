package com.project.studyenglish.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeakAttemptRequest {
    private Long userId;
    private Long categoryId;
    private Double scorePercentage;
}