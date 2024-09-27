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

        if (user.getAddress() != null && !user.getAddress().isEmpty()) {
            String address = user.getAddress();
            String[] parts = address.split(",");

            if (parts.length >= 3) {
                userResponse.setProvince(parts[0].trim());
                userResponse.setDistrict(parts[1].trim());
                userResponse.setWard(parts[2].trim());
            }
        }

        return userResponse;
    }

}
