package com.project.studyenglish.controller;

import com.project.studyenglish.dto.GrammarDto;
import com.project.studyenglish.dto.ProductDto;
import com.project.studyenglish.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("")
    private ResponseEntity<List<ProductDto>> getAllProduct(){
        List<ProductDto> productDtoList = productService.getAllProduct();
        return ResponseEntity.ok(productDtoList);
    }
    @GetMapping(value = "/by-category/{id}")
    public ResponseEntity<List<ProductDto>> getAllProductByCategoryId(@PathVariable Long id) {
        List<ProductDto> productDtoList = productService.getAllGrammarByCategory(id);
        return ResponseEntity.ok(productDtoList);
    }
}
