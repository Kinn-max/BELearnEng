package com.project.studyenglish.dto;

import com.project.studyenglish.models.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private Double price;
    private String name;
    private String description;
    private String image;
}
