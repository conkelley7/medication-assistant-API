package com.kelley.medicationassistant.openai.gateway;

import com.kelley.medicationassistant.exception.ExternalServiceException;
import com.kelley.medicationassistant.openai.dto.OpenAiRequest;
import com.kelley.medicationassistant.openai.dto.OpenAiResponse;
import com.kelley.medicationassistant.openai.feignclient.OpenAiClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;

@Component
public class OpenAiGateway {

    private final OpenAiClient openAiClient;

    public OpenAiGateway( OpenAiClient openAiClient ) {
        this.openAiClient = openAiClient;
    }

    @Retry( name = "openAi" )
    @CircuitBreaker( name = "openAi" )
    public OpenAiResponse chat( OpenAiRequest request ) {

        OpenAiResponse response = openAiClient.chat( request );

        if ( response == null
                || response.getChoices( ) == null
                || response.getChoices( ).isEmpty( )
                || response.getChoices( ).get( 0 ) == null
                || response.getChoices( ).get( 0 ).getMessage( ) == null
                || response.getChoices( ).get( 0 ).getMessage( ).getContent( ) == null ) {

            throw new ExternalServiceException(
                    "OpenAI returned an empty or malformed response"
            );
        }

        return response;
    }
}
