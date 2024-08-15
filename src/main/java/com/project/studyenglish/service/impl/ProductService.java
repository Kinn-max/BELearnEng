package com.project.studyenglish.service.impl;

import com.project.studyenglish.converter.ProductConverter;
import com.project.studyenglish.dto.ProductDto;
import com.project.studyenglish.models.ProductEntity;
import com.project.studyenglish.repository.ProductRepository;
import com.project.studyenglish.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductConverter productConverter;
    @Override
    public List<ProductDto> getAllProduct() {
        List<ProductEntity> products = productRepository.findAll();
        List<ProductDto> productDtoList = new ArrayList<>();
        for (ProductEntity productEntity : products) {
            ProductDto productDto = productConverter.toProductDto(productEntity);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    @Override
    public List<ProductDto> getAllGrammarByCategory(Long id) {
        List<ProductEntity> products = productRepository.findByCategoryEntity_Id(id);
        List<ProductDto> productDtoList = new ArrayList<>();
        for (ProductEntity productEntity : products) {
            ProductDto productDto = productConverter.toProductDto(productEntity);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }
}
