package com.project.studyenglish.models;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class ExtentOfCommon {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image",columnDefinition = "LONGTEXT")
    @Lob
    private String image;
}
