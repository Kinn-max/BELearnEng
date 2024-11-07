package com.project.studyenglish.service;

import com.project.studyenglish.dto.request.PRatingRequest;
import com.project.studyenglish.dto.response.PRatingResponse;

import java.util.List;

public interface IProductRatingService {
    List<PRatingResponse> getAllRatingByProduct(Long productId)throws Exception;
    void postProductRating(PRatingRequest ratingRequest,Long productId,Long userId) ; ;
}
