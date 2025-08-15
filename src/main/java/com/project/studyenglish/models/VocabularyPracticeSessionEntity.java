package com.project.studyenglish.models;
import com.project.studyenglish.enums.PracticeType;
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
@Table(name = "vocabulary_practice_session")
public class VocabularyPracticeSessionEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private VocabularyEntity vocabularyEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "practice_type")
    private PracticeType practiceType;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "user_answer")
    private String userAnswer;

    @Column(name = "time_taken_seconds")
    private Integer timeTakenSeconds;

    @Column(name = "practice_date")
    private Date practiceDate;
}
