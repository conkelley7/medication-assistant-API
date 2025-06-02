package com.kelley.medicationassistant.config;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration class.
 * Contains a single bean which is in use in {@link com.kelley.medicationassistant.feignclient.OpenAiClient}
 * RequestInterceptor intercepts all requests outbound to OpenAI API, adding Authorization and Content-Type headers.
 * API Key is pulled from application.properties, and is passed as Authorization header.
 */
@Configuration
public class AppConfig {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Bean
    public RequestInterceptor authInterceptor() {
        return requestTemplate -> {
                requestTemplate.header("Authorization", "Bearer " + apiKey);
                requestTemplate.header("Content-Type", "application/json");
        };
    }
}
