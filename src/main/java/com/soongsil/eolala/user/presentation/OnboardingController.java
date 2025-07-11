package com.soongsil.eolala.user.presentation;

import com.soongsil.eolala.global.support.dto.response.ApiResponse;
import com.soongsil.eolala.user.application.OnboardingService;
import com.soongsil.eolala.user.dto.request.OnboardingRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class OnboardingController {

    private final OnboardingService onboardingService;

    @Operation(summary = "사용자 온보딩 정보 저장 api", description = "사용자의 온보딩 정보를 저장합니다.")
    @PostMapping("/{userId}/onboarding")
    public ApiResponse<?> saveOnboardingInfo(
            @PathVariable Long userId,
            @Valid @RequestBody OnboardingRequest onboardingRequest
    ) {
        onboardingService.saveOnboardingInfo(userId, onboardingRequest);
        return ApiResponse.success();
    }
}
