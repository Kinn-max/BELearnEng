package com.project.studyenglish.service;

import com.project.studyenglish.dto.ProductDto;

import java.util.List;

public interface IProductService {
    List<ProductDto> getAllProduct();
    List<ProductDto> getAllGrammarByCategory(Long id);
}
