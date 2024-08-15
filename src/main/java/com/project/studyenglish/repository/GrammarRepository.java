package com.project.studyenglish.repository;
import com.project.studyenglish.models.GrammarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrammarRepository extends JpaRepository<GrammarEntity, Long> {
    List<GrammarEntity> findByCategoryEntity_Id(Long id);
}
