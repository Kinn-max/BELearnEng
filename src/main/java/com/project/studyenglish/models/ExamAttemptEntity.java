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
@Table(name = "exam_attempt")
public class ExamAttemptEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity categoryEntity;

    @Column(name = "level")
    private Integer level;

    @Column(name = "score_percentage")
    private Double scorePercentage;

    @Column(name = "total_questions")
    private Integer totalQuestions;

    @Column(name = "correct_answers")
    private Integer correctAnswers;

    @Column(name = "is_passed")
    private Boolean isPassed = false;

    @Column(name = "attempt_date")
    private Date attemptDate;

    @Column(name = "time_taken")
    private Integer timeTaken;
}