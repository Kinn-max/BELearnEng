package com.project.studyenglish.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class PRatingRequest {
    @NotNull(message = "Vui lòng chọn đánh giá sao")
    @Min(value = 1, message = "Đánh giá phải lớn hơn hoặc bằng 1")
    @Max(value = 5, message = "Đánh giá không vượt quá 5")
    private int rating;
    @NotBlank(message = "Nội dung đánh giá không được để trống")
    private String review;
}
