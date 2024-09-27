package com.project.studyenglish.service;

import com.project.studyenglish.dto.ProductDto;
import com.project.studyenglish.dto.request.ProductRequest;
import com.project.studyenglish.models.ProductEntity;

import java.util.List;

public interface IProductService {
    List<ProductDto> getAllProduct();
    List<ProductDto> getAllGrammarByCategory(Long id);
    ProductEntity createProduct(ProductRequest productRequest);
    ProductEntity updateProduct(ProductRequest productRequest);
    ProductEntity getProductById(Long id);
    void deleteProduct(Long id);
    List<ProductDto> getAllProductByCategoryAndStatus(Long id);
    ProductDto getProductDtoById(Long id);
    List<ProductDto> getRandomProduct();
}
