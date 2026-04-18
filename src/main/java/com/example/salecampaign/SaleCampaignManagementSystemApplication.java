package com.example.salecampaign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SaleCampaignManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaleCampaignManagementSystemApplication.class, args);
	}

}
