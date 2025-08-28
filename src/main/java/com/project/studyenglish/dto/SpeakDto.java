package com.project.studyenglish.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpeakDto {
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
}

