package com.project.studyenglish.repository;

import com.project.studyenglish.models.ProductEntity;
import com.project.studyenglish.models.VocabularyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByCategoryEntity_Id(Long id);
}
