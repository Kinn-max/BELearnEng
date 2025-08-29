package com.project.studyenglish.repository;

import com.project.studyenglish.models.UserSavedVocabularyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserSavedVocabularyRepository extends JpaRepository<UserSavedVocabularyEntity, Long> {
    List<UserSavedVocabularyEntity> findByUserEntityId(Long userId);
    boolean existsByUserEntityIdAndVocabularyEntityId(Long userId, Long vocabularyId);
    void deleteByUserEntityIdAndVocabularyEntityId(Long userId, Long vocabularyId);
    List<UserSavedVocabularyEntity> findByUserEntityIdAndVocabularyEntityIdIn(Long userId, List<Long> vocabularyIds);
    List<UserSavedVocabularyEntity> findAllByUserEntityId(Long userId);
    Integer countByUserEntityId(Long userId);

}
