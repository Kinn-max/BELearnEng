package com.project.studyenglish.service.impl;

import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.customexceptions.DataNotFoundException;
import com.project.studyenglish.customexceptions.PermissionDenyException;
import com.project.studyenglish.dto.request.UserRequest;
import com.project.studyenglish.models.RoleEntity;
import com.project.studyenglish.models.UserEntity;
import com.project.studyenglish.repository.RoleRepository;
import com.project.studyenglish.repository.UserRepository;
import com.project.studyenglish.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
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
}
