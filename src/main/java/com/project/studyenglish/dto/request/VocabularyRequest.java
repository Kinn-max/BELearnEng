package com.project.studyenglish.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyRequest {
    private Long id;

    @NotEmpty(message = "Tên tiếng việt không được bỏ trống")
    private String nameVietnamese;

    @NotEmpty(message = "Tên tiếng anh không được trống")
    private String name;

    @NotEmpty(message = "Phiên âm không được trống")
    private String transcription;

    @NotEmpty(message = "Thuộc từ loại không được trống")
    private String part;

    @NotEmpty(message = "Âm thanh không được trống")
    private String sound;

    @NotEmpty(message = "Ảnh không được trống")
    private String image;
    
    @NotEmpty(message = "Ví dụ không được trống")
    private String description;
}
