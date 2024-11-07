package com.project.studyenglish.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PRatingResponse {
    private Long id;
    private String fullName;
    private String image;
    private int rating;
    private String review;
    private Date commentDate;
}
