package com.project.studyenglish.service;

import com.project.studyenglish.dto.CategoryOfCommonDto;
import com.project.studyenglish.dto.request.CategoryRequest;
import com.project.studyenglish.models.CategoryEntity;

import java.util.List;

public interface ICategoryService {
    List<CategoryOfCommonDto> getAllItemOfCategory(String codeName );
    List<CategoryOfCommonDto> getAllItemOfCategoryAndStatus(String codeName);
    CategoryEntity createCategory(CategoryRequest categoryRequest);
    CategoryEntity updateCategory(CategoryRequest categoryRequest);
    void deleteCategory(Long id);
    void statusCategory(Long id);
    CategoryOfCommonDto getCategoryById(Long id);
    CategoryEntity getCategoryEntityById(Long id);
    List<CategoryOfCommonDto> getRandomCategoryOfProduct(String codeName,int number,Long userId);
    List<CategoryOfCommonDto> getAllFlashCardByUserId(String codeName,Long userId);
}
