package com.project.studyenglish.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyIncorrectResponse {
    private Long id;
    private Long progressId;
    private Long vocabularyId;
    private String vocabularyWord;
}
