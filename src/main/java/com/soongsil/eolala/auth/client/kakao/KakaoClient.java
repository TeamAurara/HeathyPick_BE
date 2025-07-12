package com.soongsil.eolala.auth.client.kakao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.soongsil.eolala.auth.client.kakao.dto.KakaoTokenResponse;
import com.soongsil.eolala.auth.client.kakao.dto.KakaoUserInfoResponse;

@Service
public class KakaoClient {

	private final WebClient tokenWebClient;
	private final WebClient apiWebClient;
	private final KakaoProperties props;

	public KakaoClient(
		@Qualifier("kakaoTokenWebClient") WebClient tokenWebClient,
		@Qualifier("kakaoApiWebClient")   WebClient apiWebClient,
		KakaoProperties props
	) {
		this.tokenWebClient = tokenWebClient;
		this.apiWebClient   = apiWebClient;
		this.props          = props;
	}

	/** 1) authorization code -> access/refresh token 교환 */
	public KakaoTokenResponse getToken(String code) {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("grant_type",   "authorization_code");
		form.add("client_id",    props.clientId());
		form.add("redirect_uri", props.redirectUri());
		form.add("code",         code);

		return tokenWebClient.post()
			.uri(uriBuilder -> uriBuilder.queryParams(form).build())
			.retrieve()
			.bodyToMono(KakaoTokenResponse.class)
			.block();
	}

	/** 2) access token -> 카카오 사용자 정보 조회 */
	public KakaoUserInfoResponse getUserInfo(String accessToken) {
		return apiWebClient.get()
			.headers(h -> h.setBearerAuth(accessToken))
			.retrieve()
			.bodyToMono(KakaoUserInfoResponse.class)
			.block();
	}
}