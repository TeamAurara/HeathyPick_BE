package com.soongsil.eolala.auth.client.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.kakao")
public record KakaoProperties(
	String clientId,
	String redirectUri
) {}