package com.project.studyenglish.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SizeOfCategoryResponse {
    private int sizeTopic;
    private int sizeExam;
    private int sizeGrammar;
    private int sizeProduct;

}
