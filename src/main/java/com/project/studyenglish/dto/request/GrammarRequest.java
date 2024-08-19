package com.project.studyenglish.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GrammarRequest {
    private Long id;
    @NotNull(message = "Không được trống")
    private Long categoryId;
    @NotBlank(message = "Tên tiêu đề không được bỏ trống")
    private String name;
    private String image;
    @NotBlank(message = "Nội dung không được bỏ trống")
    private String content;
}
