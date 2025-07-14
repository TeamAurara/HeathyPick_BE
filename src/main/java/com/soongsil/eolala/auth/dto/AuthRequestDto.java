package com.soongsil.eolala.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDto(
	@NotBlank(message = "카카오 인가 코드는 필수입니다")
	String code      // 카카오에서 발급된 authorization code
) {
}
