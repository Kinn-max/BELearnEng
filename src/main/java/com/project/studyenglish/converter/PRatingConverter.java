package com.project.studyenglish.converter;

import com.project.studyenglish.dto.response.PRatingResponse;
import com.project.studyenglish.models.ProductRatingEntity;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PRatingConverter {
    @Autowired
    private ModelMapper modelMapper;
    public PRatingResponse toPRatingResponse(ProductRatingEntity productRating) {
        PRatingResponse upRatingResponse = modelMapper.map(productRating, PRatingResponse.class);
        upRatingResponse.setFullName(productRating.getUserEntity().getFullName());
        upRatingResponse.setImage(productRating.getUserEntity().getImage());
        return upRatingResponse;

    }
}
