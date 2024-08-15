package com.project.studyenglish.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryOfCommonDto {
    private Long id;
    private String name;
    private String description;
    private Boolean status;
    private String codeName;
    private String image;
    private String createdAt;
    private int quantity;
}
