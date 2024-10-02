package com.project.studyenglish.repository;

import com.project.studyenglish.models.CategoryEntity;
import com.project.studyenglish.repository.custom.CategoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>, CategoryCustomRepository {
    @Query("SELECT COUNT(c) FROM CategoryEntity c WHERE c.codeName = :codeName")
    int countByCodeName(@Param("codeName") String codeName);

}

