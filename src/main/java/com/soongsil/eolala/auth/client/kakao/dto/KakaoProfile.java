package com.soongsil.eolala.auth.client.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

//프로필 정보
public record KakaoProfile(
	@JsonProperty("nickname")           String nickname,
	@JsonProperty("profile_image_url")  String profileImageUrl
) {}