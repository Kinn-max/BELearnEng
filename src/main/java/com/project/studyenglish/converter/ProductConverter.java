package com.project.studyenglish.converter;

import com.project.studyenglish.dto.ProductDto;
import com.project.studyenglish.models.ProductEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    @Autowired
    private ModelMapper modelMapper;
    public ProductDto toProductDto(ProductEntity productEntity){
        ProductDto productDto = modelMapper.map(productEntity, ProductDto.class);
        return productDto;
    }
}
