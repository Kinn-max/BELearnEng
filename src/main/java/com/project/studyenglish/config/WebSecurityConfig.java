package com.project.studyenglish.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static com.project.studyenglish.constant.Endpoints.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> {
                    requests
                            // Allow unauthenticated access to specific POST requests
                            .requestMatchers(POST, apiPrefix + "/category/**").permitAll()
                            .requestMatchers(POST, PUBLIC_POST_ENDPOINTS).permitAll()
                            .requestMatchers(GET, PUBLIC_GET_ENDPOINTS).permitAll()
                            .requestMatchers(DELETE, PUBLIC_DELETE_ENDPOINTS).permitAll()
                            .anyRequest().authenticated();
                });
        return http.build();
    }
}

