package com.project.studyenglish.dto;
import lombok.*;

@Getter
@Setter
@Builder
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
    private Integer level;
}
