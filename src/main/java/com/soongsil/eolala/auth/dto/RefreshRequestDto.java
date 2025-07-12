package com.soongsil.eolala.auth.dto;

/**
 * 리프레시 토큰 재발급 요청용 DTO.
 * HttpOnly 쿠키에서 꺼낸 refreshToken 값을 담아 요청.
 */
public record RefreshRequestDto(
	String refreshToken
) {}