package com.soongsil.eolala.auth.client.kakao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class KakaoWebClientConfig {

	/**
	 * 카카오 OAuth 토큰 발급(endpoint: /oauth/token) 호출용 WebClient
	 * - authorization code 를 access/refresh token 으로 교환할 때 사용
	 */
	@Bean("kakaoTokenWebClient")
	public WebClient kakaoTokenWebClient(KakaoProperties props, WebClient.Builder builder) {
		return builder
			.baseUrl(props.tokenUri())
			.defaultHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.build();
	}

	/**
	 * 카카오 사용자 정보 조회(endpoint: /v2/user/me) 호출용 WebClient
	 * - 발급받은 access token 으로 사용자 프로필/이메일 등 정보 조회할 때 사용
	 */
	@Bean("kakaoApiWebClient")
	public WebClient kakaoApiWebClient(KakaoProperties props, WebClient.Builder builder) {
		return builder
			.baseUrl(props.userInfoUri())
			.build();
	}
}