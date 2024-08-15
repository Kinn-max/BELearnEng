package com.project.studyenglish.repository;

import com.project.studyenglish.models.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {
    List<ExamEntity> findByCategoryEntity_Id(Long id);
}
