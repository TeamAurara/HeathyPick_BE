package com.soongsil.eolala.user.dto.request;

import com.soongsil.eolala.user.domain.User;
import com.soongsil.eolala.user.domain.UserOnboarding;
import com.soongsil.eolala.user.domain.type.Activity;
import com.soongsil.eolala.user.domain.type.CkdLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OnboardingRequest(
        @NotBlank String nickname,
        @NotBlank String gender,
        @Min(0) int age,
        @NotNull Activity activity,
        @Positive double height,
        @Positive double weight,
        @Positive double goalWeight,
        @NotBlank String ckdLevel
) {
    public UserOnboarding toOnboarding(User user) {
        return UserOnboarding.builder()
                .user(user)
                .height(this.height())
                .weight(this.weight())
                .goalWeight(this.goalWeight())
                .activity(this.activity())
                .ckdLevel(CkdLevel.from(this.ckdLevel()))
                .build();
    }
}
