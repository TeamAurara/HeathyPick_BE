package com.soongsil.eolala.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessTokenResponseDto(
    @JsonProperty("access_token")
    String accessToken,

    @JsonProperty("expires_in")
    long expiresIn
) {
    public static AccessTokenResponseDto of(AuthResponseDto authResponse) {
        return new AccessTokenResponseDto(
            authResponse.token().accessToken(),
            authResponse.token().accessTokenExpiresIn()
        );
    }
}
