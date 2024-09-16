package com.project.studyenglish.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {
    private  Long id;
    private  String name;
    private  String image;
    private  String source;
    private  String type;
}
