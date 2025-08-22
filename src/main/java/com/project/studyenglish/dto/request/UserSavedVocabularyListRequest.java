package com.project.studyenglish.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserSavedVocabularyListRequest {
    private Long userId;
    private List<Long> vocabularies;
}
