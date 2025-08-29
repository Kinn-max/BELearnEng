package com.project.studyenglish.converter;

import com.project.studyenglish.dto.response.UserResponse;
import com.project.studyenglish.models.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    @Autowired
    private ModelMapper modelMapper;
    public UserResponse toUserResponse(UserEntity user) {
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);

        return userResponse;
    }

}
