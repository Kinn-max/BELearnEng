package com.project.studyenglish.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyDto {
    private Long id;
    private String nameVietnamese;
    private String transcription;
    private String part;
    private String sound;
    private String name;
    private String description;
    private String image;
    private Boolean isFavorite;
}
