package com.soongsil.eolala.domain.auth.dto;

/**
 * 백엔드에서 프론트엔드로 반환할 토큰 및 유저 정보
 */
public record AuthResponseDto(
	String accessToken,           // 새로 발급된 액세스 토큰
	String refreshToken,          // 발급된 리프레시 토큰
	Long   accessTokenExpiresIn,  // Access Token 만료까지 남은 초
	Long   refreshTokenExpiresIn, // Refresh Token 만료까지 남은 초
	Long   userId,                // 내부 사용자 식별자
	String email,                 // 카카오에서 가져온 이메일
	String nickname,              // 카카오에서 가져온 닉네임
	String profileImageUrl        // 카카오 프로필 이미지 URL
) {}