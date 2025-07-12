
package com.soongsil.eolala.auth.application;

import com.soongsil.eolala.auth.client.kakao.KakaoClient;
import com.soongsil.eolala.auth.client.kakao.dto.KakaoTokenResponse;
import com.soongsil.eolala.auth.client.kakao.dto.KakaoUserInfoResponse;
import com.soongsil.eolala.auth.domain.RefreshToken;
import com.soongsil.eolala.auth.dto.AuthRequestDto;
import com.soongsil.eolala.auth.dto.AuthResponseDto;
import com.soongsil.eolala.auth.dto.RefreshRequestDto;
import com.soongsil.eolala.auth.dto.TokenDto;
import com.soongsil.eolala.auth.dto.UserInfoDto;
import com.soongsil.eolala.auth.persistence.RefreshTokenRepository;
import com.soongsil.eolala.auth.util.JwtProvider;
import com.soongsil.eolala.user.application.UserService;
import com.soongsil.eolala.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

	private final KakaoClient kakaoClient;
	private final UserService userService;
	private final TokenService tokenService;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtProvider jwtProvider;

	public AuthResponseDto loginWithKakao(AuthRequestDto request) {
		// 1) 인가 코드 → 카카오 토큰 교환
		KakaoTokenResponse tokenResp = kakaoClient.getToken(request.code());

		// 2) Access Token → 카카오 사용자 정보 조회
		KakaoUserInfoResponse kakaoUser = kakaoClient.getUserInfo(tokenResp.accessToken());

		// 3) 우리 DB에 사용자 저장 또는 조회
		User user = userService.findOrCreate(kakaoUser);

		// 4) TokenService를 사용하여 JWT 토큰 생성
		TokenDto tokens = tokenService.createTokens(user);

		// 5) 사용자 정보 DTO 생성
		UserInfoDto userInfo = new UserInfoDto(
			user.getId(),
			user.getEmail(),
			user.getNickname(),
			user.getProfileImageUrl(),
			user.getGender(),
			user.getAge(),
			user.isOnboarded()
		);

		// 6) 응답 DTO 반환
		return new AuthResponseDto(tokens, userInfo);
	}

	// refresh 토큰 재발급 메서드도 유사한 구조로 구현해야징
}