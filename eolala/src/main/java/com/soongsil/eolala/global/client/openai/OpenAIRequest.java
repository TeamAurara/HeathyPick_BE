package com.soongsil.eolala.global.client.openai;

public record OpenAIRequest(
        Contents contents
) {
    public record Contents(
            Parts parts
    ) {
        public record Parts(
                String text
        ) {
        }
    }
}
