package com.project.studyenglish.dto.response;

import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpeakDataResponse {
    private Long id;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String translation;
}
