package com.project.studyenglish.repository.custom;

import com.project.studyenglish.dto.response.UserResponse;

import java.util.List;

public interface UserCustomRepository {
    List<UserResponse> getAllUser();
}
