package com.project.studyenglish.repository;

import com.project.studyenglish.models.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByCategoryEntity_Id(Long id);
    @Query(value = "SELECT * FROM product ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<ProductEntity> findRandomProduct();
}
