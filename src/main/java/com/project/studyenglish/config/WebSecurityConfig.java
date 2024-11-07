package com.project.studyenglish.config;


import com.project.studyenglish.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                                    String.format("%s/search/**", apiPrefix),
                                    String.format("%s/user/activation/**", apiPrefix),
                                    String.format("%s/product-rating/**", apiPrefix)
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
                            //user
                            .requestMatchers(GET,  "api/user").hasRole("ADMIN")
                            .requestMatchers(DELETE,  "api/user/**").hasRole("ADMIN")
                            .requestMatchers(POST,  "api/user/status/**").hasRole("ADMIN")
                            .requestMatchers(GET,  "api/user/search/**").hasRole("ADMIN")
                            .requestMatchers(GET,  "api/user/by-token").hasRole("USER")
                            .requestMatchers(POST,  "api/user/by-token").hasRole("USER")
                            .requestMatchers(POST,  "api/product-rating/**").hasRole("USER")
                            .requestMatchers(POST,  "api/user/**").hasRole("ADMIN")
                            //add cart
                            .requestMatchers(POST,  "api/add-cart").hasRole("USER")
                            .requestMatchers(GET,  "api/add-cart").hasRole("USER")
                            .requestMatchers(DELETE,  "api/add-cart/**").hasRole("USER")
                            //order
                            .requestMatchers(GET,  "api/user/**").hasAnyRole("USER","ADMIN")
                            .requestMatchers(POST,  "api/order").hasRole("USER")
                            .requestMatchers(GET,  "api/order/by-user").hasRole("USER")
                            .requestMatchers(GET,  "api/order").hasAnyRole("ADMIN","STAFF")
                            .requestMatchers(GET,  "api/order/admin").hasAnyRole("ADMIN","STAFF")
                            .requestMatchers(POST,  "api/order/**").hasAnyRole("ADMIN","USER","STAFF")
                            //search
//                            .requestMatchers(GET,  "api/search/home/size-show").hasAnyRole("ADMIN","STAFF")
                            .anyRequest().authenticated();
                });
        return http.build();
    }
}

