package com.project.studyenglish.controller;

import com.project.studyenglish.dto.request.UserLogin;
import com.project.studyenglish.dto.request.UserRequest;
import com.project.studyenglish.models.UserEntity;
import com.project.studyenglish.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private IUserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest userRequest,
                                      BindingResult result) {
        try {
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userRequest.getPassword().equals(userRequest.getConfirmPassword())){
                return ResponseEntity.badRequest().body("Password not match");
            }
            userService.createUser(userRequest);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Create account successfully");
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLogin userLogin) {
        try {
            String token = userService.login(userLogin.getEmail(), userLogin.getPassword());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
