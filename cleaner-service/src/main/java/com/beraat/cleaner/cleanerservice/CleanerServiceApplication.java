package com.beraat.cleaner.cleanerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaAuditing
@EnableEurekaClient
public class CleanerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CleanerServiceApplication.class, args);

	}
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

}
