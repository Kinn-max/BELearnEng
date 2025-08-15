package com.project.studyenglish.controller;

import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.dto.UserDto;
import com.project.studyenglish.dto.request.PasswordCreationRequest;
import com.project.studyenglish.dto.request.UserLogin;
import com.project.studyenglish.dto.request.UserRequest;
import com.project.studyenglish.dto.response.LoginResponse;
import com.project.studyenglish.dto.response.UserResponse;
import com.project.studyenglish.models.UserEntity;
import com.project.studyenglish.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private IUserService userService;
    @PostMapping("/outbound/authentication")
    ResponseEntity<?> outboundAuthentication(@RequestParam("code") String code) {
        try {
            LoginResponse result = userService.outboundAuthenticate(code);

            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/create-password")
    ResponseEntity<?> createPassword(@RequestBody @Valid PasswordCreationRequest request,  BindingResult result) {
        if(result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }
        userService.updatePassword(request);
        return ResponseEntity.ok("Password updated");
    }

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
            UserEntity userEntity = userService.createUser(userRequest);
            Map<String, Long> response = new HashMap<>();
            response.put("id", userEntity.getId());

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
    @GetMapping("")
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        try {

            List<UserResponse> userResponseList = userService.getAllUsers();
            Map<String, List> response = new HashMap<>();
            response.put("data", userResponseList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/status/{id}")
    public ResponseEntity<?> setStatusUser(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            Long userId = jwtTokenUtil.extractUserId(token);
            if(userId == null){
                return ResponseEntity.badRequest().body("You need to log in first!");
            }
            userService.setStatusUser(id);
            return ResponseEntity.ok("");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            Long userId = jwtTokenUtil.extractUserId(token);
            if(userId == null){
                return ResponseEntity.badRequest().body("You need to log in first!");
            }
            userService.deleteUser(id);
            return ResponseEntity.ok("");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping("/search")
    public List<UserResponse> search(@RequestParam String name) {
        return userService.searchUserByNameOrEmail(name);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserResponse userResponse = userService.getUserById(id);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/by-token")
    public ResponseEntity<?> getUserByToken( HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            Long userId = jwtTokenUtil.extractUserId(token);
            if(userId == null){
                return ResponseEntity.badRequest().body("You need to log in first!");
            }
            UserResponse userResponse = userService.getUserById(userId);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/by-token")
    public ResponseEntity<?> updateUserByToken(@RequestBody UserDto userDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            Long userId = jwtTokenUtil.extractUserId(token);
            if(userId == null){
                return ResponseEntity.badRequest().body("You need to log in first!");
            }
            userService.updateUser(userId,userDto);
            return ResponseEntity.ok("Ok");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/{role}/{id}")
    public ResponseEntity<?> updateRoleOfUser(@PathVariable("role") Long roleId,@PathVariable("id") Long id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            Long userId = jwtTokenUtil.extractUserId(token);
            if(userId == null){
                return ResponseEntity.badRequest().body("You need to log in first!");
            }
            userService.setRoleOfUser(roleId,id);
            return ResponseEntity.ok("Set role successful!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/activation/{userId}/{code}")
    public ResponseEntity<?> activationAccount(
            @PathVariable("userId") Long userId,
            @PathVariable("code") Long code) {
        try {
            boolean result = userService.activationAccount(userId, code);
            Map<String, String> response = new HashMap<>();
            if (result) {
                response.put("message", "Activation Success!");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Activation Failed!");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }



}
