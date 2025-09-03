package com.example.tnovel_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TnovelBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TnovelBackendApplication.class, args);
	}

}
