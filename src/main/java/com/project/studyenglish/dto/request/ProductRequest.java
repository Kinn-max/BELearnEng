package com.project.studyenglish.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private Long id;

    @NotNull(message = "ID Category error")
    private Long categoryId;

    @NotBlank(message = "Name is Required")
    @Size(min = 3,max=100,message = "Title must be between 3 and 200 characters")
    private String name;

    @Min(value=0,message = "Price must be greater than or equal 0")
    @Max(value=1000000000,message = "Price must be less than or equal 1000000000")
    private Double price;

    private String description;
    @NotBlank(message = "Image is required")
    private String image;
}
