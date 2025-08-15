package com.project.studyenglish.service;

import com.project.studyenglish.dto.UserDto;
import com.project.studyenglish.dto.request.PasswordCreationRequest;
import com.project.studyenglish.dto.request.UserRequest;
import com.project.studyenglish.dto.response.LoginResponse;
import com.project.studyenglish.dto.response.UserResponse;
import com.project.studyenglish.models.UserEntity;
import lombok.Lombok;

import java.util.List;

public interface IUserService {
    UserEntity createUser(UserRequest user) throws Exception;
    String login(String phoneNumber, String password) throws Exception;
    List<UserResponse> getAllUsers() throws Exception;
    void setStatusUser(Long id) throws Exception;
    void deleteUser(Long id) throws Exception;
    List<UserResponse> searchUserByNameOrEmail(String keySearch) ;
    UserResponse getUserById(Long id) throws Exception;
    void updateUser(Long id, UserDto userDto) throws Exception;
    void setRoleOfUser(Long role,Long id) throws Exception;
    boolean activationAccount(Long userId,Long code) throws Exception;
    LoginResponse outboundAuthenticate(String code) throws Exception;
    void updatePassword(PasswordCreationRequest request);
}
