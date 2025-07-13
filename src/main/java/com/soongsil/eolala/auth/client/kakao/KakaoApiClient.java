package com.soongsil.eolala.auth.client.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.soongsil.eolala.auth.client.kakao.dto.KakaoUserInfoResponse;

@FeignClient(name = "kakao-api", url = "https://kapi.kakao.com")
public interface KakaoApiClient {

	@GetMapping("/v2/user/me")
	KakaoUserInfoResponse getUserInfo(@RequestHeader("Authorization") String authorization);
} 