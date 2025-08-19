package com.project.studyenglish.dto.request;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VocabularyIncorrectRequest {
    private Long id;
    private Long vocabularyId;
}

