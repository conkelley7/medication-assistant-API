package com.kelley.medicationassistant.openai.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    @Value( "${spring.ai.openai.api-key}" )
    private String apiKey;

    @Bean
    public RequestInterceptor authInterceptor( ) {

        return requestTemplate -> {
            requestTemplate.header( "Authorization", "Bearer " + apiKey );
            requestTemplate.header( "Content-Type", "application/json" );
        };

    }
}
