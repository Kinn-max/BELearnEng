package com.project.studyenglish.service;

import com.project.studyenglish.dto.request.UserLogin;
import com.project.studyenglish.dto.request.UserRequest;
import com.project.studyenglish.models.UserEntity;

public interface IUserService {
    UserEntity createUser(UserRequest user) throws Exception;
    String login(String phoneNumber, String password) throws Exception;
}
