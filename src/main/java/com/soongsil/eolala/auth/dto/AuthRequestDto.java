package com.soongsil.eolala.auth.dto;

/**
 * 프론트엔드로부터 전달받은 카카오 인가 코드 정보
 */
public record AuthRequestDto(
	String code      // 카카오에서 발급된 authorization code
) {
}
