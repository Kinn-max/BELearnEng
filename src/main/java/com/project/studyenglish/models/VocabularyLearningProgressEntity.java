package com.project.studyenglish.models;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vocabulary_learning_progress")
public class VocabularyLearningProgressEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private VocabularyEntity vocabularyEntity;

    @Column(name = "mastery_level")
    private Integer masteryLevel = 0;

    @Column(name = "correct_count")
    private Integer correctCount = 0;

    @Column(name = "incorrect_count")
    private Integer incorrectCount = 0;

    @Column(name = "total_attempts")
    private Integer totalAttempts = 0;

    @Column(name = "success_rate")
    private Double successRate = 0.0; // %

    @Column(name = "last_reviewed_date")
    private Date lastReviewedDate;

    @Column(name = "next_review_date")
    private Date nextReviewDate; // Spaced repetition

    @Column(name = "review_interval_days")
    private Integer reviewIntervalDays = 1;
}