package com.project.studyenglish.repository;

import com.project.studyenglish.models.ProductEntity;
import com.project.studyenglish.models.ProductRatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRatingRepository extends JpaRepository<ProductRatingEntity, Long> {
    List<ProductRatingEntity> findByProductEntity_Id(Long productId);
}
