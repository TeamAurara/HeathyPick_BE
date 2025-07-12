package com.soongsil.eolala.auth.presentation;

import com.soongsil.eolala.auth.application.AuthService;
import com.soongsil.eolala.auth.dto.AuthRequestDto;
import com.soongsil.eolala.auth.dto.AccessTokenResponseDto;
import com.soongsil.eolala.auth.dto.AuthResponseDto;
import com.soongsil.eolala.auth.dto.RefreshRequestDto;
import com.soongsil.eolala.auth.application.CookieService;
import com.soongsil.eolala.global.securitiy.annotation.AuthenticatedApi;
import com.soongsil.eolala.global.support.dto.response.ApiResponse;
import com.soongsil.eolala.global.support.error.GlobalErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;

    @Operation(summary = "카카오 로그인", description = "카카오 인가 코드를 사용하여 로그인합니다.")
    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<AuthResponseDto>> loginWithKakao(
            @Valid @RequestBody AuthRequestDto request,
            HttpServletResponse response) {
        
        log.info("카카오 로그인 시작 - code: {}", request.code().substring(0, 10) + "...");
        
        AuthResponseDto authResponse = authService.loginWithKakao(request);
        
        cookieService.setRefreshTokenCookie(response, authResponse.token().refreshToken(), authResponse.token().refreshTokenExpiresIn());
        
        log.info("카카오 로그인 완료 - userId: {}", authResponse.user().userId());
        
        return ResponseEntity.ok(ApiResponse.success(authResponse.withoutRefreshToken()));
    }

    @Operation(summary = "토큰 갱신", description = "Refresh Token을 사용하여 새로운 Access Token을 발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AccessTokenResponseDto>> refreshToken(
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response) {
        
        if (refreshToken == null || refreshToken.isBlank()) {
            log.warn("Refresh Token이 누락됨");
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(GlobalErrorType.INVALID_REQUEST_ARGUMENT));
        }
        
        log.info("토큰 갱신 시작");
        
        AuthResponseDto authResponse = authService.refreshToken(new RefreshRequestDto(refreshToken));
        
        cookieService.setRefreshTokenCookie(response, authResponse.token().refreshToken(), authResponse.token().refreshTokenExpiresIn());
        
        log.info("토큰 갱신 완료 - userId: {}", authResponse.user().userId());
        
        return ResponseEntity.ok(ApiResponse.success(AccessTokenResponseDto.of(authResponse)));
    }

    @Operation(summary = "로그아웃", description = "사용자를 로그아웃하고 Refresh Token을 무효화합니다.")
    @PostMapping("/logout")
    @AuthenticatedApi
    public ResponseEntity<ApiResponse<Object>> logout(
            Authentication authentication,
            HttpServletResponse response) {
        
        Long userId = Long.parseLong(authentication.getName());
        log.info("로그아웃 시작 - userId: {}", userId);
        
        authService.logout(userId);
        
        cookieService.clearRefreshTokenCookie(response);
        
        log.info("로그아웃 완료 - userId: {}", userId);
        
        return ResponseEntity.ok(ApiResponse.success());
    }
} 