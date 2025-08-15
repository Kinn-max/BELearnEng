package com.project.studyenglish.models;
import com.project.studyenglish.enums.LearningPriority;
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
@Table(name = "user_saved_vocabulary")
public class UserSavedVocabularyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private VocabularyEntity vocabularyEntity;

    @Column(name = "saved_date")
    private Date savedDate;

    @Column(name = "is_learned")
    private Boolean isLearned = false;

    @Column(name = "learned_date")
    private Date learnedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private LearningPriority priority = LearningPriority.MEDIUM;

    @Column(name = "notes")
    private String notes;
}