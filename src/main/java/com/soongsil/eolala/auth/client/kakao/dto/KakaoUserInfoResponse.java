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
		// account가 null인 경우 안전하게 처리
		if (account == null) {
			return null;
		}
		return account.email();
	}

	@Override
	public String getNickname() {
		// account가 null이거나 profile이 null인 경우 안전하게 처리
		if (account == null || account.profile() == null) {
			return "Unknown";
		}
		return account.profile().nickname();
	}
}