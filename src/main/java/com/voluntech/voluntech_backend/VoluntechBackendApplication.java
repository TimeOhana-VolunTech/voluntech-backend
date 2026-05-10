package com.voluntech.voluntech_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VoluntechBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoluntechBackendApplication.class, args);
	}

}
