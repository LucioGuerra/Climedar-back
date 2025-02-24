package com.climedar.consultation_sv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.climedar")
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
public class ConsultationSvApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsultationSvApplication.class, args);
	}

}
