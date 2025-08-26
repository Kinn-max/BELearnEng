package com.project.studyenglish.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GrammarOverView {
    private Long id;
    private String name;
    private String image;
}