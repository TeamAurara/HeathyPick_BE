package com.soongsil.eolala.auth.dto;

/**
 * 백엔드에서 프론트엔드로 반환할
 * – TokenDto + UserInfoDto
 */
public record AuthResponseDto(
	TokenDto    token,
	UserInfoDto user
) {}