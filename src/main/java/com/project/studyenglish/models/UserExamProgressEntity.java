package com.project.studyenglish.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_exam_progress")
public class UserExamProgressEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Column(name = "max_level_unlocked")
    private Integer maxLevelUnlocked = 1;

    @Column(name = "current_level")
    private Integer currentLevel = 1;

    @Column(name = "total_levels")
    private Integer totalLevels;
}
