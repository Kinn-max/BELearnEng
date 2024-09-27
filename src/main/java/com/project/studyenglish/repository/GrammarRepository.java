package com.project.studyenglish.repository;
import com.project.studyenglish.models.GrammarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GrammarRepository extends JpaRepository<GrammarEntity, Long> {
    List<GrammarEntity> findByCategoryEntity_Id(Long id);

    @Query(value = "SELECT * FROM grammar ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<GrammarEntity> findRandomGrammar();
}
