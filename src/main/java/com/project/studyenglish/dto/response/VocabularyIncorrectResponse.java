package com.project.studyenglish.dto.response;

import com.project.studyenglish.dto.VocabularyDto;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyIncorrectResponse {
    private Long progressId;
    List<VocabularyDto> vocabularies;
}
