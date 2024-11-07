package com.project.studyenglish.controller;


import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.dto.request.PRatingRequest;
import com.project.studyenglish.dto.request.ProductRequest;
import com.project.studyenglish.dto.response.PRatingResponse;
import com.project.studyenglish.service.impl.ProductRatingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "api/product-rating")
public class PRatingController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ProductRatingService productRatingService;
    @GetMapping("/{id}")
    public ResponseEntity<?> getAllProductRating(@PathVariable Long id)  {
        Map<String, List> response = new HashMap<>();
        try {
            List<PRatingResponse> pRatingResponses = productRatingService.getAllRatingByProduct(id);
            response.put("comment", pRatingResponses);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}")
    public ResponseEntity<?> postComment(@PathVariable Long id, @Valid @RequestBody PRatingRequest pRatingRequest, BindingResult result , HttpServletRequest request)  {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtTokenUtil.extractUserId(token);
        if(userId == null){
            return ResponseEntity.badRequest().body(Map.of("message", "You need to log in first!"));
        }
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessages);
            }
            productRatingService.postProductRating(pRatingRequest,id,userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Comment successful");
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
