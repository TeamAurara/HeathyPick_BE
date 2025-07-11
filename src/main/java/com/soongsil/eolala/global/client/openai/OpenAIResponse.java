package com.soongsil.eolala.global.client.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public record OpenAIResponse(
        List<Candidate> candidates
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Candidate(
            Content content
    ) {
        public record Content(
                List<Part> parts,
                String role
        ) {
            public record Part(
                    String text
            ) {

            }
        }
    }
}
