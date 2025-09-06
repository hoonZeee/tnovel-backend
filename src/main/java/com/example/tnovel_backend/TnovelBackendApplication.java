package com.example.tnovel_backend;

import com.example.tnovel_backend.configuration.PortOneConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(PortOneConfig.class)
public class TnovelBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TnovelBackendApplication.class, args);
    }

}
