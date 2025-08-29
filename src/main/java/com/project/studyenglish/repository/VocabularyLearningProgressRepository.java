package com.project.studyenglish.repository;


import com.project.studyenglish.models.VocabularyLearningProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VocabularyLearningProgressRepository extends JpaRepository<VocabularyLearningProgressEntity, Long> {
    Optional<VocabularyLearningProgressEntity> findByUserEntityIdAndCategoryEntityId(Long userId, Long categoryId);
    List<VocabularyLearningProgressEntity> findAllByUserEntityId(Long userId);

}