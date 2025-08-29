package com.project.studyenglish.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String createdAt;
    private boolean active;
    private String phoneNumber;
    private String address;
    private String email;
    private String image;
}
