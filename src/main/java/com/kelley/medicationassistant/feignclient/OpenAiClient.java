package com.kelley.medicationassistant.feignclient;

import com.kelley.medicationassistant.config.AppConfig;
import com.kelley.medicationassistant.payload.OpenAiRequest;
import com.kelley.medicationassistant.payload.OpenAiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * FeignClient for making requests to OpenAI API.
 * Uses {@link AppConfig} for configuration to add authorization and content-type headers for each outbound request.
 */
@FeignClient(name = "openAiClient", url="https://api.openai.com/v1", configuration = AppConfig.class)
public interface OpenAiClient {

    @PostMapping(value = "/chat/completions")
    OpenAiResponse chat(@RequestBody OpenAiRequest request);
}
