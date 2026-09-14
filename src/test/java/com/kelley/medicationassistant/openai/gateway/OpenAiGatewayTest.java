package com.kelley.medicationassistant.openai.gateway;

import com.kelley.medicationassistant.exception.ExternalServiceException;
import com.kelley.medicationassistant.openai.dto.OpenAiRequest;
import com.kelley.medicationassistant.openai.feignclient.OpenAiClient;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "spring.ai.openai.api-key=test-key",
        "medicationassistant.jwtsecret=01234567890123456789012345678901",

        // Make retry nearly instantaneous
        "resilience4j.retry.instances.openAi.max-attempts=3",
        "resilience4j.retry.instances.openAi.wait-duration=1ms",
        "resilience4j.retry.instances.openAi.retry-exceptions=" +
                "com.kelley.medicationassistant.exception.ExternalServiceException",

        // Open after three failed calls for this test
        "resilience4j.circuitbreaker.instances.openAi.sliding-window-size=3",
        "resilience4j.circuitbreaker.instances.openAi.minimum-number-of-calls=3",
        "resilience4j.circuitbreaker.instances.openAi.failure-rate-threshold=100",
        "resilience4j.circuitbreaker.instances.openAi.wait-duration-in-open-state=1m"
})
@ActiveProfiles("local")
class OpenAiGatewayTest {

    @Autowired
    private OpenAiGateway openAiGateway;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockitoBean
    private OpenAiClient openAiClient;

    @BeforeEach
    void resetCircuitBreaker( ) {
        circuitBreakerRegistry.circuitBreaker( "openAi" ).reset( );
    }

    @Test
    void chat_whenOpenAiRepeatedlyFails_retriesAndOpensCircuit( ) {

        // A null response causes the gateway to throw ExternalServiceException.
        when( openAiClient.chat( any( OpenAiRequest.class ) ) )
                .thenReturn( null );

        OpenAiRequest request = new OpenAiRequest( );

        // Initial attempt plus two retries all fail.
        assertThrows(
                ExternalServiceException.class,
                ( ) -> openAiGateway.chat( request )
        );

        verify( openAiClient, times( 3 ) ).chat( request );

        CircuitBreaker circuitBreaker =
                circuitBreakerRegistry.circuitBreaker( "openAi" );

        assertEquals(
                CircuitBreaker.State.OPEN,
                circuitBreaker.getState( )
        );

        // The next call is rejected without reaching OpenAI.
        assertThrows(
                CallNotPermittedException.class,
                ( ) -> openAiGateway.chat( request )
        );

        // Still three calls: the open circuit blocked the fourth.
        verify( openAiClient, times(3) ).chat( request );
    }
}
