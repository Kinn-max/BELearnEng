package com.project.studyenglish.service.impl;

import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.converter.UserConverter;
import com.project.studyenglish.customexceptions.DataNotFoundException;
import com.project.studyenglish.customexceptions.InvalidParamException;
import com.project.studyenglish.customexceptions.PermissionDenyException;
import com.project.studyenglish.dto.UserDto;
import com.project.studyenglish.dto.request.ExchangeTokenRequest;
import com.project.studyenglish.dto.request.PasswordCreationRequest;
import com.project.studyenglish.dto.request.UserRequest;
import com.project.studyenglish.dto.request.UserUpdateFullName;
import com.project.studyenglish.dto.response.ApiMessageResponse;
import com.project.studyenglish.dto.response.LoginResponse;
import com.project.studyenglish.dto.response.UserResponse;
import com.project.studyenglish.models.RoleEntity;
import com.project.studyenglish.models.UserEntity;
import com.project.studyenglish.repository.httpclient.OutboundIdentityClient;
import com.project.studyenglish.repository.RoleRepository;
import com.project.studyenglish.repository.UserRepository;
import com.project.studyenglish.repository.httpclient.OutboundUserClient;
import com.project.studyenglish.service.IEmailService;
import com.project.studyenglish.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final OutboundIdentityClient outboundIdentityClient;
    private final OutboundUserClient outboundUserClient;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private IEmailService emailService;
    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected  String CLIENT_ID ;
    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected  String CLIENT_SECRET;
    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    protected  String REDIRECT_URI ;
    @NonFinal
    protected final String GRANT_TYPE = "authorization_code";
    @Override
    public UserEntity createUser(UserRequest user) throws PermissionDenyException {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DataNotFoundException("Email already exists");
        }

        RoleEntity role = roleRepository.findById(user.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));

        if ("ADMIN".equalsIgnoreCase(role.getName())) {
            throw new PermissionDenyException("Admin role cannot be created");
        }

        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        UserEntity newUser = UserEntity.builder()
                .email(user.getEmail())
                .password(encryptedPassword)
                .roleEntity(role)
                .activationCode(generateActivationCode())
                .build();

        userRepository.save(newUser);

        emailService.sendMessage(
                "no-reply@studyenglish.com",
                newUser.getEmail(),
                "Activate your account",
                "Mã kích hoạt của bạn là: " + newUser.getActivationCode()
        );

        return newUser;
    }

    private int generateActivationCode() {
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }


    @Override
    public LoginResponse login(String email, String password) throws Exception {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Invalid email / password"));

        if (existingUser.getFacebookAccountId() == 0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Wrong email or password");
            }
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        email, password, existingUser.getAuthorities()
                );
        authenticationManager.authenticate(authenticationToken);

        return LoginResponse.builder()
                .userId(existingUser.getId())
                .fullName(existingUser.getFullName())
                .token(jwtTokenUtil.generateToken(existingUser))
                .activation(existingUser.isActive())
                .hasPassword(StringUtils.hasText(existingUser.getPassword()))
                .firstLogin(!existingUser.isActive() || !StringUtils.hasText(existingUser.getPassword()))
                .build();
    }


    @Override
    public List<UserResponse> getAllUsers() throws Exception {
        List<UserEntity> users = userRepository.findByRoleEntityNot();
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

    @Override
    public boolean activationAccount(Long userId, Long code) throws Exception {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        if (userEntity.isActive()) {
            return true;
        }

        if (userEntity.getActivationCode() == code) {
            userEntity.setActive(true);
            userRepository.save(userEntity);
            return true;
        }
        return false;
    }

    @Override
    public LoginResponse outboundAuthenticate(String code) throws Exception {
        var response = outboundIdentityClient.exchangeToken(
                ExchangeTokenRequest.builder()
                        .code(code)
                        .clientId(CLIENT_ID)
                        .clientSecret(CLIENT_SECRET)
                        .redirectUri(REDIRECT_URI)
                        .grantType(GRANT_TYPE)
                        .build()
        );
        log.info("Outbound authenticate response: {}", response);
        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());
        RoleEntity role =roleRepository.findById(2L)
                .orElseThrow(() -> new Exception("Role not found"));

        Optional<UserEntity> optionalUser = userRepository.findByEmail(userInfo.getEmail());
        UserEntity user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = UserEntity.builder()
                    .googleAccountId(userInfo.getId())
                    .email(userInfo.getEmail())
                    .fullName(userInfo.getName())
                    .active(true)
                    .activationCode(9999)
                    .image(userInfo.getPicture())
                    .roleEntity(role)
                    .build();
            userRepository.save(user);
        }

        LoginResponse loginResponse =  LoginResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .token( jwtTokenUtil.generateToken(user))
                .activation(user.isActive())
                .hasPassword(StringUtils.hasText(user.getPassword()))
                .firstLogin(!user.isActive() || !StringUtils.hasText(user.getPassword()))
                .build();
        return loginResponse;
    }
    public void updatePassword(PasswordCreationRequest request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(name).orElseThrow(() -> new ApplicationContextException("User not found"));
        if(StringUtils.hasText(user.getPassword())){
            throw new ApplicationContextException("Password already taken");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Override
    public String renderToken(String email, String password) throws Exception {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Invalid email / password"));

        if (existingUser.getFacebookAccountId() == 0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Wrong email or password");
            }
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        email, password, existingUser.getAuthorities()
                );
        authenticationManager.authenticate(authenticationToken);

        return jwtTokenUtil.generateToken(existingUser);
    }


    @Override
    public void updateFullName(Long id, UserUpdateFullName req) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found with id: " + id));
        user.setFullName(req.getFullName());
        userRepository.save(user);
    }

    @Override
    public ApiMessageResponse validationToken(String token) throws Exception {

        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new InvalidParamException("Token expired");
        }

        Long userId = jwtTokenUtil.extractUserId(token);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newToken = jwtTokenUtil.generateToken(user);

        return ApiMessageResponse.builder()
                .token(newToken)
                .build();
    }


}
