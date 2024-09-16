package com.project.studyenglish.repository;

import com.project.studyenglish.models.CategoryEntity;
import com.project.studyenglish.repository.custom.CategoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>, CategoryCustomRepository {
}

