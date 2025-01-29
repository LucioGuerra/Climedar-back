package com.climedar.doctor_sv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.climedar")
@EnableFeignClients
@EnableDiscoveryClient
@EnableJpaAuditing
public class DoctorSvApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorSvApplication.class, args);
	}

}
