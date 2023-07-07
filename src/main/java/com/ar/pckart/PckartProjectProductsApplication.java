package com.ar.pckart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
//@EnableDiscoveryClient
public class PckartProjectProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PckartProjectProductsApplication.class, args);
	}

	@PostConstruct
	public void addCategory() {
		
	}
	
}
