package com.project.studyenglish.repository.custom;

import com.project.studyenglish.models.CategoryEntity;

import java.util.List;

public interface CategoryCustomRepository {
    List<CategoryEntity> getAllOptionsCategory(String categoryCode);
}
