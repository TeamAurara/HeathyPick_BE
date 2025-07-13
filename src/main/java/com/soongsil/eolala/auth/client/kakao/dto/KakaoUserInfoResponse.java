package com.soongsil.eolala.auth.client.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soongsil.eolala.auth.client.OAuthUserInfo;

public record KakaoUserInfoResponse(
	@JsonProperty("id")            Long         id,
	@JsonProperty("kakao_account") KakaoAccount account
) implements OAuthUserInfo {
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getEmail() {
		return account.email();
	}

	@Override
	public String getNickname() {
		return account.profile().nickname();
	}
}