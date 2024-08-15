package com.project.studyenglish.service.impl;

import com.project.studyenglish.dto.CategoryOfCommonDto;
import com.project.studyenglish.dto.request.CategoryRequest;
import com.project.studyenglish.models.CategoryEntity;
import com.project.studyenglish.repository.CategoryRepository;
import com.project.studyenglish.service.ICategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<CategoryOfCommonDto> getAllItemOfCategory(String codeName) {
        List<CategoryEntity> categoryEntityList = categoryRepository.getAllOptionsCategory(codeName);
        List<CategoryOfCommonDto> categoryOfCommonDtoList = new ArrayList<>();
        for (CategoryEntity category : categoryEntityList) {
            CategoryOfCommonDto categoryOfCommonDto = modelMapper.map(category, CategoryOfCommonDto.class);
            if(category.getExamEntityList() != null && category.getExamEntityList().size() > 0) {
                categoryOfCommonDto.setQuantity(category.getExamEntityList().size());
            }
            if (category.getVocabularyEntityList() != null && category.getVocabularyEntityList().size() > 0) {
                categoryOfCommonDto.setQuantity(category.getVocabularyEntityList().size());
            }
            if(category.getGrammarEntityList() != null && category.getGrammarEntityList().size() > 0) {
                categoryOfCommonDto.setQuantity(category.getGrammarEntityList().size());
            }
            if(category.getProductEntityList() != null && category.getProductEntityList().size() > 0) {
                categoryOfCommonDto.setQuantity(category.getProductEntityList().size());
            }
            categoryOfCommonDtoList.add(categoryOfCommonDto);
        }
        return categoryOfCommonDtoList;
    }

    @Override
    public CategoryEntity createCategory(CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = CategoryEntity
                .builder()
                .id(categoryRequest.getId())
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .image(categoryRequest.getImage())
                .codeName(categoryRequest.getCodeName())
                .status(true)
                .build();
        return categoryRepository.save(categoryEntity);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void statusCategory(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id).get();
        categoryEntity.setStatus(categoryEntity.getStatus() ? false : true);
        categoryRepository.save(categoryEntity);
    }

    @Override
    public CategoryOfCommonDto getCategoryById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id).get();
        return modelMapper.map(categoryEntity, CategoryOfCommonDto.class);
    }
}
