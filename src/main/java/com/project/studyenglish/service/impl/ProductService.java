package com.project.studyenglish.service.impl;

import com.project.studyenglish.converter.ProductConverter;
import com.project.studyenglish.customexceptions.DataNotFoundException;
import com.project.studyenglish.dto.ProductDto;
import com.project.studyenglish.dto.request.ProductRequest;
import com.project.studyenglish.models.CategoryEntity;
import com.project.studyenglish.models.ProductEntity;
import com.project.studyenglish.repository.CategoryRepository;
import com.project.studyenglish.repository.ProductRepository;
import com.project.studyenglish.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductConverter productConverter;
    @Autowired
    private CategoryRepository categoryRepository;
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

    @Override
    public ProductEntity createProduct(ProductRequest productRequest) throws DataIntegrityViolationException {
        CategoryEntity existingCategory = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new DataIntegrityViolationException("Category not found"));
        ProductEntity productEntity = ProductEntity.builder()
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .image(productRequest.getImage())
                .name(productRequest.getName())
                .categoryEntity(existingCategory)
                .build();
        return productRepository.save(productEntity);
    }

    @Override
    public ProductEntity getProductById(Long id) {
        return productRepository.findById(id).
                orElseThrow(()-> new DataNotFoundException(
                        "Cannot find product with id ="+id));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDto> getAllProductByCategoryAndStatus(Long id) {
        List<ProductEntity> productEntity = productRepository.findByCategoryEntity_Id(id);
        List<ProductDto> productResult = new ArrayList<>();
        for (ProductEntity product : productEntity) {
            if(product.getCategoryEntity().getStatus() == true){
                 ProductDto productDto = productConverter.toProductDto(product);
                 productResult.add(productDto);
            }
        }
        return productResult;
    }

    @Override
    public ProductDto getProductDtoById(Long id) {
        ProductEntity productEntity = productRepository.findById(id).get();
        ProductDto productDto = productConverter.toProductDto(productEntity);
        return productDto;
    }

    @Override
    public ProductEntity updateProduct(ProductRequest productRequest) {
        ProductEntity existingProduct = getProductById(productRequest.getId());
        if(existingProduct != null) {
            CategoryEntity existingCategory = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new DataIntegrityViolationException("Category not found"));
            existingProduct.setPrice(productRequest.getPrice());
            existingProduct.setDescription(productRequest.getDescription());
            if(productRequest.getImage() != null) {
                existingProduct.setImage(productRequest.getImage());
            }
            existingProduct.setName(productRequest.getName());
            existingProduct.setCategoryEntity(existingCategory);
            return productRepository.save(existingProduct);
        }
        return null;
    }

}
