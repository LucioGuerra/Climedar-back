package com.climedar.medical_service_sv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.climedar")
@EnableDiscoveryClient
@EnableFeignClients
public class MedicalServiceSvApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalServiceSvApplication.class, args);
	}

}
