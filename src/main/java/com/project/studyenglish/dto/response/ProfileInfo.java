package com.project.studyenglish.dto.response;

import lombok.*;

import java.util.Date;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileInfo {
    private Long id;
    private String userImage;
    private Double score;
    private int challenge;
    private int lesson;

}

