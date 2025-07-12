package com.soongsil.eolala.auth.client.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoProperties(
	String clientId,
	String redirectUri,
	String tokenUri,
	String userInfoUri
) {}