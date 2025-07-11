package com.soongsil.eolala.global.client.openai;

import lombok.Builder;

import java.util.List;

@Builder
public record OpenAIRequest(
        List<Content> contents
) {
    public record Content(
            List<Part> parts
    ) {
        public record Part(
                String text
        ) {
        }
    }
}
