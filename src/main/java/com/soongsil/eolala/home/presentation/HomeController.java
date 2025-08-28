package com.soongsil.eolala.home.presentation;

import com.soongsil.eolala.global.securitiy.annotation.AuthenticatedApi;
import com.soongsil.eolala.global.support.dto.response.ApiResponse;
import com.soongsil.eolala.home.application.HomeService;
import com.soongsil.eolala.home.dto.response.HomeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "홈 화면", description = "홈 화면 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @AuthenticatedApi(reason = "사용자의 홈 화면 정보 조회를 위해 인증 필요")
    @Operation(summary = "홈 화면 조회", description = "사용자의 일일 칼로리 및 영양소 섭취 현황을 조회합니다.")
    @GetMapping("/{userId}")
    public ApiResponse<HomeResponse> getHomeSummary(@PathVariable Long userId) {
        HomeResponse response = homeService.getHomeSummary(userId);
        return ApiResponse.success(response);
    }
}