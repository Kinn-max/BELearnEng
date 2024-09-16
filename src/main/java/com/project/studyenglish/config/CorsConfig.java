package com.project.studyenglish.config;

import com.project.studyenglish.constant.Endpoints;
import com.project.studyenglish.constant.NameOfCategory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// Cấu hình cho tất cả các endpoint fe
                .allowedOrigins("http://localhost:3001")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
