package com.example.tnovel_backend.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "portone")
@Getter
@Setter
public class PortOneConfig {

    private String baseUrl;
    private final Rest rest = new Rest();

    @Getter
    @Setter
    public static class Rest {
        private String apiKey;
        private String apiSecret;
    }
}
