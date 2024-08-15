package com.project.studyenglish.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name_category")
    @NotEmpty(message = "Tên danh mục không được trống")
    private String name;

    @JsonProperty("description")
    @NotEmpty(message = "Mô tả danh mục không được trống")
    private String description;

    @JsonProperty("code_name")
    @NotEmpty(message = "Mã danh mục không được trống")
    private String codeName;
    private String image;
}
