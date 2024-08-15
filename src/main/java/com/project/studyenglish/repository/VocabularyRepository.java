package com.project.studyenglish.repository;

import com.project.studyenglish.models.VocabularyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VocabularyRepository extends JpaRepository<VocabularyEntity, Long> {
    List<VocabularyEntity> findByCategoryEntity_Id(Long id);
}
