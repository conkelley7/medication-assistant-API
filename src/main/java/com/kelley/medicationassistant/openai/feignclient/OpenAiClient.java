package com.kelley.medicationassistant.openai.feignclient;

import com.kelley.medicationassistant.openai.config.OpenAIConfig;
import com.kelley.medicationassistant.openai.dto.OpenAiRequest;
import com.kelley.medicationassistant.openai.dto.OpenAiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * FeignClient for making requests to OpenAI API.
 */
@FeignClient( name = "openAiClient", url="https://api.openai.com/v1", configuration = OpenAIConfig.class )
public interface OpenAiClient {

    @PostMapping( value = "/chat/completions" )
    OpenAiResponse chat(@RequestBody OpenAiRequest request );
}
