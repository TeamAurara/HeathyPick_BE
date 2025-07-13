package com.soongsil.eolala.auth.dto;

public record RefreshedTokensDto(
    String accessToken,
    String refreshToken,
    long accessTokenExpiresIn,
    long refreshTokenExpiresIn,
    Long userId
) {
}
