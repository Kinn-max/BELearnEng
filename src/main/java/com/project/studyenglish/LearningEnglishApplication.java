package com.project.studyenglish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;


@EnableFeignClients
@SpringBootApplication
public class LearningEnglishApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningEnglishApplication.class, args);
	}
	@Bean
	public RestClientCustomizer restClientCustomizer(Logbook logbook) {
		return restClientBuilder -> restClientBuilder.requestInterceptor(new LogbookClientHttpRequestInterceptor(logbook));
	}
}
