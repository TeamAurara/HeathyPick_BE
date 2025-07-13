package com.soongsil.eolala.auth.dto;

public record TokenDto(
	String accessToken,
	String refreshToken,
	Long   accessTokenExpiresIn,
	Long   refreshTokenExpiresIn
) {}