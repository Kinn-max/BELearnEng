package com.project.studyenglish.config;

import com.project.studyenglish.constant.Endpoints;
import com.project.studyenglish.constant.NameAccess;
import com.project.studyenglish.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static com.project.studyenglish.constant.Endpoints.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    String.format("%s/user/register", apiPrefix),
                                    String.format("%s/user/login", apiPrefix),
                                    String.format("%s/search", apiPrefix)
                            ).permitAll()
                            .requestMatchers(GET, PUBLIC_GET_ENDPOINTS).permitAll()
                            .requestMatchers(POST, PUBLIC_POST_ENDPOINTS).permitAll()
                            //category
                            .requestMatchers(POST,  "api/category/**").hasRole("ADMIN")
                            .requestMatchers(POST,  "api/category/status/**").hasRole("ADMIN")
                            .requestMatchers(DELETE,"api/category/**").hasRole("ADMIN")
                            .requestMatchers(PUT,  "api/category/**").hasRole("ADMIN")
                            // vocabulary config
                            .requestMatchers(POST,  "api/vocabulary").hasRole("ADMIN")
                            .requestMatchers(PUT,  "api/vocabulary/**").hasRole("ADMIN")
                            .requestMatchers(DELETE, "api/vocabulary/**").hasRole("ADMIN")
                            // product
                            .requestMatchers(POST,  "api/product").hasRole("ADMIN")
                            .requestMatchers(PUT,  "api/product/**").hasRole("ADMIN")
                            .requestMatchers(DELETE,  "api/product/**").hasRole("ADMIN")
                            //exam
                            .requestMatchers(POST,  "api/exam").hasRole("ADMIN")
                            .requestMatchers(PUT,  "api/exam/**").hasRole("ADMIN")
                            .requestMatchers(DELETE,  "api/exam/**").hasRole("ADMIN")
                            //grammar
                            .requestMatchers(POST,  "api/grammar").hasRole("ADMIN")
                            .requestMatchers(PUT,  "api/grammar/**").hasRole("ADMIN")
                            .requestMatchers(DELETE,  "api/grammar/**").hasRole("ADMIN")
                            .anyRequest().authenticated();
                });
        return http.build();
    }
}

