package com.project.studyenglish.controller;

import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.constant.NameOfCategory;
import com.project.studyenglish.dto.CategoryOfCommonDto;
import com.project.studyenglish.dto.request.CategoryRequest;
import com.project.studyenglish.dto.response.UserFavotiteResponse;
import com.project.studyenglish.repository.UserSavedVocabularyRepository;
import com.project.studyenglish.service.impl.CategoryService;
import com.project.studyenglish.service.impl.VocabularyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Transactional
@RequestMapping(value = "api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserSavedVocabularyRepository userSavedVocabularyRepository;
    @GetMapping(value =  "/vocabulary")
    public ResponseEntity<List<CategoryOfCommonDto>> getAllCategoryOfVocabulary() {
        List<CategoryOfCommonDto> vocabularyDtoList = categoryService.getAllItemOfCategory(NameOfCategory.VOCABULARY);
        return ResponseEntity.ok(vocabularyDtoList);
    }
    @GetMapping(value =  "/vocabulary/status")
    public ResponseEntity<List<CategoryOfCommonDto>> getAllCategoryOfVocabularyAndStatus() {
        List<CategoryOfCommonDto> vocabularyDtoList = categoryService.getAllItemOfCategoryAndStatus(NameOfCategory.VOCABULARY);
        return ResponseEntity.ok(vocabularyDtoList);
    }
    @GetMapping(value =  "/exam")
    public ResponseEntity<List<CategoryOfCommonDto>> getAllCategoryOfExam() {
        List<CategoryOfCommonDto> categoryOfExamDtoList = categoryService.getAllItemOfCategory(NameOfCategory.EXAM);
        return ResponseEntity.ok(categoryOfExamDtoList);
    }
    @GetMapping(value =  "/exam/status")
    public ResponseEntity<List<CategoryOfCommonDto>> getAllCategoryOfExamAndStatus() {
        List<CategoryOfCommonDto> categoryOfExamDtoList = categoryService.getAllItemOfCategoryAndStatus(NameOfCategory.EXAM);
        return ResponseEntity.ok(categoryOfExamDtoList);
    }
    @GetMapping(value =  "/product")
    public ResponseEntity<List<CategoryOfCommonDto>> getAllCategoryOfProduct() {
        List<CategoryOfCommonDto> categoryOfProductDtoList = categoryService.getAllItemOfCategory(NameOfCategory.PRODUCT);
        return ResponseEntity.ok(categoryOfProductDtoList);
    }
    @GetMapping(value =  "/product/status")
    public ResponseEntity<List<CategoryOfCommonDto>> getAllCategoryOfProductAndStatus() {
        List<CategoryOfCommonDto> categoryOfProductDtoList = categoryService.getAllItemOfCategoryAndStatus(NameOfCategory.PRODUCT);
        return ResponseEntity.ok(categoryOfProductDtoList);
    }
    @GetMapping(value =  "/grammar")
    public ResponseEntity<List<CategoryOfCommonDto>> getAllCategoryOfGrammar() {
        List<CategoryOfCommonDto> categoryOfProductDtoList = categoryService.getAllItemOfCategory(NameOfCategory.GRAMMAR);
        return ResponseEntity.ok(categoryOfProductDtoList);
    }
    @GetMapping(value =  "/grammar/status")
    public ResponseEntity<List<CategoryOfCommonDto>> getAllCategoryOfGrammarAndStatus() {
        List<CategoryOfCommonDto> categoryOfProductDtoList = categoryService.getAllItemOfCategoryAndStatus(NameOfCategory.GRAMMAR);
        return ResponseEntity.ok(categoryOfProductDtoList);
    }
    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest categoryRequest, BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.createCategory(categoryRequest);
        return ResponseEntity.ok("Ok");
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable long id, @RequestBody CategoryRequest categoryRequest) {
        try {
            categoryService.updateCategory(categoryRequest);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Update category successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete category with id: "+id+" successfully");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/status/{id}")
    public ResponseEntity<?> statusCategory(@PathVariable Long id) {
        categoryService.statusCategory(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "The status of the category has changed.");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try{
           CategoryOfCommonDto categoryOfCommonDto = categoryService.getCategoryById(id);
            return ResponseEntity.ok(categoryOfCommonDto);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi xảy ra");
        }

    }
    @GetMapping(value =  "/vocabulary/status/random")
    public ResponseEntity<UserFavotiteResponse> getAllCategoryOfVocabularyAndStatusRandom(@Param("number") int number, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtTokenUtil.extractUserId(token);
        List<CategoryOfCommonDto> categoryOfCommonDtos = categoryService.getRandomCategoryOfProduct(NameOfCategory.VOCABULARY,number);
        Integer numberFavorite = userSavedVocabularyRepository.countByUserEntityId(userId);
        if (numberFavorite == null) {
            numberFavorite = 0;
        }
        UserFavotiteResponse userHomeFlashCard = UserFavotiteResponse.builder()
                .favoriteNumber(numberFavorite)
                .categoryOfCommonDto(categoryOfCommonDtos)
                .build();
        return ResponseEntity.ok(userHomeFlashCard);
    }

}
