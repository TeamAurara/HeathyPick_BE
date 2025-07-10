package com.soongsil.eolala.user.presentation;

import com.soongsil.eolala.global.support.dto.response.ApiResponse;
import com.soongsil.eolala.user.application.OnboardingService;
import com.soongsil.eolala.user.dto.request.OnboardingRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class OnboardingController {

    private final OnboardingService onboardingService;

    @PostMapping("/{userId}/onboarding")
    public ApiResponse<?> updateOnboardingInfo(
            @PathVariable Long userId,
            @Valid @RequestBody OnboardingRequest onboardingRequest
    ) {
        onboardingService.updateOnboardingInfo(userId, onboardingRequest);
        return ApiResponse.success();
    }
}
