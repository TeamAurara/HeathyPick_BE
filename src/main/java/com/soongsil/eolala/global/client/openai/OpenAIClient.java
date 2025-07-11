package com.soongsil.eolala.global.client.openai;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "openai", url = "${open-ai.google.url}")
public interface OpenAIClient {

    @PostMapping(value = "${open-ai.google.path}", consumes = MediaType.APPLICATION_JSON_VALUE)
    OpenAIResponse openAIRequest(
            @RequestHeader("X-goog-api-key") String apiKey,
            @RequestBody OpenAIRequest request
    );
}
