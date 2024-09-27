package com.project.studyenglish.service.impl;

import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.converter.UserConverter;
import com.project.studyenglish.customexceptions.DataNotFoundException;
import com.project.studyenglish.customexceptions.PermissionDenyException;
import com.project.studyenglish.dto.UserDto;
import com.project.studyenglish.dto.request.UserRequest;
import com.project.studyenglish.dto.response.UserResponse;
import com.project.studyenglish.models.CategoryEntity;
import com.project.studyenglish.models.RoleEntity;
import com.project.studyenglish.models.UserEntity;
import com.project.studyenglish.repository.RoleRepository;
import com.project.studyenglish.repository.UserRepository;
import com.project.studyenglish.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    @Autowired
    private UserConverter userConverter;

    @Override
    public UserEntity createUser(UserRequest user) throws Exception  {
        String email = user.getEmail();
        if(userRepository.existsByEmail(email)){
            throw new DataNotFoundException("Email already exists");
        }
        RoleEntity role =roleRepository.findById(user.getRoleId())
                .orElseThrow(() -> new Exception("Role not found"));
        if(role.getName().toUpperCase().equals("ADMIN")){
            throw new PermissionDenyException("Admin role cannot be created");
        }
        UserEntity newUser = UserEntity.builder()
                .fullName(user.getFullName())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
        newUser.setRoleEntity(role);
        String encryptedPassword = user.getPassword();
        newUser.setPassword(passwordEncoder.encode(encryptedPassword));
        return userRepository.save(newUser);
    }

    @Override
    public String login(String email, String password) throws Exception{
        UserEntity existingUser = userRepository.findByEmail(email).get();
        if(existingUser == null) {
            throw new DataNotFoundException("Invalid phone number / password");
        }
        if(existingUser.isActive() == false){
            throw new DataNotFoundException("You have not activated your account.");
        }
        //check password
        if (existingUser.getFacebookAccountId() == 0
                && existingUser.getGoogleAccountId() == 0) {
            if(!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Wrong email or password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email, password,
                existingUser.getAuthorities()
        );

        //authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public List<UserResponse> getAllUsers() throws Exception {
        RoleEntity roleEntity1 = roleRepository.findById(2L).get();
        RoleEntity roleEntity2 = roleRepository.findById(3L).get();
        List<UserEntity> users = userRepository.findByRoleEntityOrRoleEntity(roleEntity1, roleEntity2);
        List<UserResponse> userResponseList = new ArrayList<>();
        for (UserEntity user : users) {
            UserResponse userResponse = modelMapper.map(user, UserResponse.class);
            userResponseList.add(userResponse);
        }
        return userResponseList;
    }

    @Override
    public void setStatusUser(Long id) throws Exception {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
        userEntity.setActive(!userEntity.isActive());
        userRepository.save(userEntity);
    }

    @Override
    public void deleteUser(Long id) throws Exception {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
        userRepository.delete(userEntity);
    }

    @Override
    public List<UserResponse> searchUserByNameOrEmail(String keySearch) {
        List<UserEntity> users = userRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keySearch,keySearch);
        List<UserResponse> userResponseList = new ArrayList<>();
        for (UserEntity user : users) {
            UserResponse userResponse = modelMapper.map(user, UserResponse.class);
            userResponseList.add(userResponse);
        }
        return userResponseList;
    }

    @Override
    public UserResponse getUserById(Long id) throws Exception {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
        UserResponse userResponse = userConverter.toUserResponse(userEntity);
        return userResponse;
    }

    @Override
    public void updateUser(Long id, UserDto user) throws Exception {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
        userEntity.setFullName(user.getFullName());
        userEntity.setPhoneNumber(user.getPhoneNumber());
        userEntity.setAddress(user.getAddress());
        userRepository.save(userEntity);
    }

    @Override
    public void setRoleOfUser(Long role, Long id) throws  Exception {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
        RoleEntity roleEntity = roleRepository.findById(role).orElseThrow(() -> new Exception("Role not found"));
        userEntity.setRoleEntity(roleEntity);
        userRepository.save(userEntity);
    }

}
