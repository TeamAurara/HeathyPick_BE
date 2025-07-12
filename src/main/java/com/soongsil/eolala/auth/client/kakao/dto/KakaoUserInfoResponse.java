package com.soongsil.eolala.auth.client.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

//사용자 응답
public record KakaoUserInfoResponse(
	@JsonProperty("id")             Long         id,
	@JsonProperty("kakao_account")  KakaoAccount account
) {}
