package com.project.studyenglish.repository;

import com.project.studyenglish.models.CategoryEntity;
import com.project.studyenglish.models.ExamEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ExamRepository extends JpaRepository<ExamEntity, Long> {
    Page<ExamEntity> findByCategoryEntity_Id(Long id , Pageable pageable);
    List<ExamEntity> findByCategoryEntity_Id(Long id);
}
