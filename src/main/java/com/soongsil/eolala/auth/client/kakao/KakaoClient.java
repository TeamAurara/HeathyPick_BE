package com.soongsil.eolala.auth.client.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soongsil.eolala.auth.client.kakao.dto.KakaoTokenResponse;


@FeignClient(name = "kakao-token", url = "https://kauth.kakao.com")
public interface KakaoClient {

	/**
	 * authorization code -> access/refresh token 교환
	 */
	@PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	KakaoTokenResponse getToken(
		@RequestParam("grant_type") String grantType,
		@RequestParam("client_id") String clientId,
		@RequestParam("client_secret") String clientSecret,
		@RequestParam("redirect_uri") String redirectUri,
		@RequestParam("code") String code
	);
}