package com.microservices.springproductsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class SpringProductsServiceApplication {

	@Bean
	WebClient webClient() {
		return WebClient.builder().build();
	}

	@Bean
    String queueName() {
        return "imageResizeQueue";
    }

	public static void main(String[] args) {
		SpringApplication.run(SpringProductsServiceApplication.class, args);
	}

}
