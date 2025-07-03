package com.soongsil.eolala.global.client.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openai.google")
public record OpenAIProperties(
        String apiKey,
        String url,
        String path
) {
}
