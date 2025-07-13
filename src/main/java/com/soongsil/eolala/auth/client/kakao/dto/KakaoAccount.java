package com.soongsil.eolala.auth.client.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

//계정정보
public record KakaoAccount(
	@JsonProperty("profile") KakaoProfile profile,
	@JsonProperty("email")   String       email
) {}
