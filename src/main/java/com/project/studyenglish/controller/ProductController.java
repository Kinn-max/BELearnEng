package com.project.studyenglish.controller;

import com.project.studyenglish.dto.CategoryOfCommonDto;
import com.project.studyenglish.dto.GrammarDto;
import com.project.studyenglish.dto.ProductDto;
import com.project.studyenglish.dto.request.ProductRequest;
import com.project.studyenglish.service.impl.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @PostMapping("")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest productRequest, BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessages);
            }
            productService.createProduct(productRequest);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Insert product successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable long id,
            @RequestBody ProductRequest productRequest) {
        try {
             productService.updateProduct(productRequest);
             Map<String, String> response = new HashMap<>();
             response.put("message", "Update product successfully");
             return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete product with id: "+id+" successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/by-category/{id}/status")
    public ResponseEntity<?> getAllProductByCategoryStatus(@PathVariable Long id) {
        try {
            List<ProductDto> result= productService.getAllProductByCategoryAndStatus(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    private ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            ProductDto productDto = productService.getProductDtoById(id);
            return ResponseEntity.ok(productDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
