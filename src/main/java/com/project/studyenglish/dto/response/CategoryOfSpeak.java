package com.project.studyenglish.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryOfSpeak {
    private Long id;
    private String name;
    private String description;
    private Boolean status;
    private String codeName;
    private String image;
    private String createdAt;
    private int quantity;
    private Integer level;
    private boolean done;
    private Double averageScore;
}
