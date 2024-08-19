package com.project.studyenglish.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "ID Topic cannot be null")
    private  Long idTopic;

    @NotEmpty(message = "Tên tiếng việt không được bỏ trống")
    private String nameVietnamese;

    @NotEmpty(message = "Tên tiếng anh không được trống")
    private String name;

    @NotEmpty(message = "Phiên âm không được trống")
    private String transcription;

    @NotEmpty(message = "Thuộc từ loại không được trống")
    private String part;

    private String sound;

    private String image;
    
    @NotEmpty(message = "Ví dụ không được trống")
    private String description;
}
