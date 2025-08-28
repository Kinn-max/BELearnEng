package com.project.studyenglish.repository;

import com.project.studyenglish.models.SpeakAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpeakAttemptRepository extends JpaRepository<SpeakAttemptEntity, Long> {
    boolean existsByUserEntityIdAndCategoryEntityId(Long userId, Long categoryId);
    @Query("SELECT AVG(sa.scorePercentage) FROM SpeakAttemptEntity sa " +
            "WHERE sa.userEntity.id = :userId AND sa.categoryEntity.id = :categoryId")
    Double findAverageScoreByUserAndCategory(@Param("userId") Long userId,
                                             @Param("categoryId") Long categoryId);
}