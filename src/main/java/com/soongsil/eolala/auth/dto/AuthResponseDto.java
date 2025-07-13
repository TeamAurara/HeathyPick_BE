package com.soongsil.eolala.auth.dto;

public record AuthResponseDto(
	TokenDto    token,
	UserInfoDto user
) {

	public AuthResponseDto withoutRefreshToken() {
		TokenDto tokenWithoutRefresh = new TokenDto(
			token.accessToken(),
			null,
			token.accessTokenExpiresIn(),
			token.refreshTokenExpiresIn()
		);
		return new AuthResponseDto(tokenWithoutRefresh, user);
	}
}