package com.soongsil.eolala.auth.client.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

//토큰 교환 응답
public record KakaoTokenResponse(
	@JsonProperty("access_token")            String accessToken,
	@JsonProperty("refresh_token")           String refreshToken,
	@JsonProperty("expires_in")              Long   expiresIn,
	@JsonProperty("refresh_token_expires_in") Long   refreshTokenExpiresIn
) {}
