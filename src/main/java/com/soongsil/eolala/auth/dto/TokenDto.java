package com.soongsil.eolala.auth.dto;

/**
 * 액세스·리프레시 토큰 정보만 담는 DTO
 */
public record TokenDto(
	String accessToken,
	String refreshToken,
	Long   accessTokenExpiresIn,
	Long   refreshTokenExpiresIn
) {}