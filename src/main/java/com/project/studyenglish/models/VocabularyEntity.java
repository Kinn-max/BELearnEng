package com.project.studyenglish.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "vocabulary")
public class VocabularyEntity extends ExtentOfCommon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_vietnamese")
    private String nameVietnamese;

    @Column(name = "transcription")
    private String transcription;

    @Column(name = "part")
    private String part;

    @Column(name = "name_english")
    private String nameEnglish;
    @Column(name = "sound",columnDefinition = "LONGTEXT")
    @Lob
    private String sound;
    @Column(name = "image", columnDefinition = "LONGTEXT")
    @Lob
    private String image;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;
}
