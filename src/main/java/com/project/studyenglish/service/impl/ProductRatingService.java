package com.project.studyenglish.service.impl;

import com.project.studyenglish.converter.PRatingConverter;
import com.project.studyenglish.converter.ProductConverter;
import com.project.studyenglish.dto.request.PRatingRequest;
import com.project.studyenglish.dto.response.PRatingResponse;
import com.project.studyenglish.models.ProductEntity;
import com.project.studyenglish.models.ProductRatingEntity;
import com.project.studyenglish.models.UserEntity;
import com.project.studyenglish.repository.ProductRatingRepository;
import com.project.studyenglish.repository.ProductRepository;
import com.project.studyenglish.repository.UserRepository;
import com.project.studyenglish.service.IProductRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductRatingService implements IProductRatingService {
    @Autowired
    private ProductRatingRepository productRatingRepository;
    @Autowired
    private PRatingConverter pRatingConverter;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<PRatingResponse> getAllRatingByProduct(Long productId) throws Exception {
        List<PRatingResponse> responseList = new ArrayList<>();
        List<ProductRatingEntity> productRatingList =  productRatingRepository.findByProductEntity_Id(productId);
        for (ProductRatingEntity productRatingEntity : productRatingList) {
            PRatingResponse pratingResponse = pRatingConverter.toPRatingResponse(productRatingEntity);
            responseList.add(pratingResponse);
        }
        return responseList;
    }

    @Override
    public void postProductRating(PRatingRequest ratingRequest, Long productId, Long userId) {
        ProductEntity productEntity = productRepository.findById(productId).get();
        UserEntity userEntity = userRepository.findById(userId).get();
        LocalDateTime now = LocalDateTime.now();
        ProductRatingEntity pRatingEntity =  ProductRatingEntity.builder()
                .productEntity(productEntity)
                .userEntity(userEntity)
                .rating(ratingRequest.getRating())
                .review(ratingRequest.getReview())
                .commentDate(new Date())
                .build();
        productRatingRepository.save(pRatingEntity);

    }
}
