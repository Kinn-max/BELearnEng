package com.project.studyenglish.models;

import com.project.studyenglish.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "exam")
public class ExamEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer_a")
    private String answerA;

    @Column(name = "answer_b")
    private String answerB;

    @Column(name = "answer_c")
    private String answerC;

    @Column(name = "answer_d")
    private String answerD;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "image", columnDefinition = "LONGTEXT")
    @Lob
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private DifficultyLevel difficulty = DifficultyLevel.EASY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity categoryEntity;

    @Column(name = "question_order")
    private Integer questionOrder;
}