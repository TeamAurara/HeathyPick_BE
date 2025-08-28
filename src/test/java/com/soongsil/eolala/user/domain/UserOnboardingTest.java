package com.soongsil.eolala.user.domain;

import com.soongsil.eolala.user.domain.type.Activity;
import com.soongsil.eolala.user.domain.type.CkdLevel;
import com.soongsil.eolala.user.domain.type.Gender;
import com.soongsil.eolala.user.domain.type.Role;
import com.soongsil.eolala.user.domain.type.SocialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class UserOnboardingTest {

    private User createUser(Gender gender, int age) {
        return User.builder()
                .email("test@test.com")
                .providerId("12345")
                .socialType(SocialType.KAKAO)
                .nickname("테스터")
                .gender(gender)
                .age(age)
                .profileImageUrl("profile.jpg")
                .isOnboarded(false)
                .role(Role.USER)
                .build();
    }

    @Test
    @DisplayName("30세 남성, 보통 활동량, 체중 감량 목표일 때 칼로리 계산")
    void calculateDailyCalories_Male_ModerateActivity_WeightLoss() {
        // given
        User user = createUser(Gender.MALE, 30);
        UserOnboarding onboarding = UserOnboarding.builder()
                .user(user)
                .activity(Activity.MOVE_WELL)
                .height(175)
                .weight(80)
                .goalWeight(75)
                .ckdLevel(CkdLevel.LEVEL_1)
                .build();
        user.updateOnboarding(onboarding);

        // when
        int dailyCalories = onboarding.calculateDailyCalories();

        // then
        assertThat(dailyCalories).isEqualTo(2211);
    }

    @Test
    @DisplayName("25세 여성, 가벼운 활동량, 체중 유지 목표일 때 칼로리 계산")
    void calculateDailyCalories_Female_LightActivity_Maintain() {
        // given
        User user = createUser(Gender.FEMALE, 25);
        UserOnboarding onboarding = UserOnboarding.builder()
                .user(user)
                .activity(Activity.MOVE_LESS)
                .height(165)
                .weight(60)
                .goalWeight(60)
                .ckdLevel(CkdLevel.LEVEL_1)
                .build();
        user.updateOnboarding(onboarding);

        // when
        int dailyCalories = onboarding.calculateDailyCalories();

        // then
        assertThat(dailyCalories).isEqualTo(1850);
    }

    @Test
    @DisplayName("35세 남성, 격한 활동량, 체중 증량 목표일 때 칼로리 계산")
    void calculateDailyCalories_Male_VeryActive_WeightGain() {
        // given
        User user = createUser(Gender.MALE, 35);
        UserOnboarding onboarding = UserOnboarding.builder()
                .user(user)
                .activity(Activity.MOVE_HARD)
                .height(180)
                .weight(70)
                .goalWeight(75)
                .ckdLevel(CkdLevel.LEVEL_1)
                .build();
        user.updateOnboarding(onboarding);

        // when
        int dailyCalories = onboarding.calculateDailyCalories();

        // then
        assertThat(dailyCalories).isEqualTo(3155);
    }

    @Test
    @DisplayName("40세 여성, 좌식 생활, 체중 감량 목표일 때 최소 칼로리 보장")
    void calculateDailyCalories_Female_Sedentary_MinimumCalories() {
        // given
        User user = createUser(Gender.FEMALE, 40);
        UserOnboarding onboarding = UserOnboarding.builder()
                .user(user)
                .activity(Activity.MOVE_NONE)
                .height(160)
                .weight(55)
                .goalWeight(45)
                .ckdLevel(CkdLevel.LEVEL_2)
                .build();
        user.updateOnboarding(onboarding);

        // when
        int dailyCalories = onboarding.calculateDailyCalories();

        // then
        assertThat(dailyCalories).isEqualTo(1200);
    }

    @Test
    @DisplayName("50세 남성, 좌식 생활, 체중 감량 목표일 때 최소 칼로리 보장")
    void calculateDailyCalories_Male_Sedentary_MinimumCalories() {
        // given
        User user = createUser(Gender.MALE, 50);
        UserOnboarding onboarding = UserOnboarding.builder()
                .user(user)
                .activity(Activity.MOVE_NONE)
                .height(170)
                .weight(75)
                .goalWeight(65)
                .ckdLevel(CkdLevel.LEVEL_3A)
                .build();
        user.updateOnboarding(onboarding);

        // when
        int dailyCalories = onboarding.calculateDailyCalories();

        // then
        assertThat(dailyCalories).isGreaterThanOrEqualTo(1381);
        assertThat(dailyCalories).isLessThanOrEqualTo(1500);
    }

    @ParameterizedTest
    @CsvSource({
            "MOVE_NONE, 1.2",
            "MOVE_LESS, 1.375",
            "MOVE_WELL, 1.55",
            "MOVE_HARD, 1.725"
    })
    @DisplayName("활동 강도별 활동 계수 확인")
    void testActivityMultiplier(Activity activity, double expectedMultiplier) {
        // given
        User user = createUser(Gender.MALE, 30);
        UserOnboarding onboarding = UserOnboarding.builder()
                .user(user)
                .activity(activity)
                .height(175)
                .weight(70)
                .goalWeight(70)
                .ckdLevel(CkdLevel.LEVEL_1)
                .build();
        user.updateOnboarding(onboarding);

        // when
        int dailyCalories = onboarding.calculateDailyCalories();
        
        // then
        int expectedCalories = (int) Math.round(1648.75 * expectedMultiplier);
        assertThat(dailyCalories).isEqualTo(expectedCalories);
    }

    @Test
    @DisplayName("체중 목표에 따른 칼로리 조정 - 0.5kg 차이는 체중 유지로 간주")
    void testCalorieAdjustment_SmallDifference() {
        // given
        User user = createUser(Gender.MALE, 30);
        UserOnboarding onboarding = UserOnboarding.builder()
                .user(user)
                .activity(Activity.MOVE_WELL)
                .height(175)
                .weight(70)
                .goalWeight(70.5)
                .ckdLevel(CkdLevel.LEVEL_1)
                .build();
        user.updateOnboarding(onboarding);

        // when
        int dailyCalories = onboarding.calculateDailyCalories();

        // then
        assertThat(dailyCalories).isEqualTo(2556);
    }

    @Test
    @DisplayName("여러 연령대의 칼로리 계산 비교")
    void testCaloriesAcrossAges() {
        // given
        Activity activity = Activity.MOVE_WELL;
        double height = 175;
        double weight = 70;
        double goalWeight = 70;
        
        User youngUser = createUser(Gender.MALE, 20);
        UserOnboarding youngOnboarding = UserOnboarding.builder()
                .user(youngUser)
                .activity(activity)
                .height(height)
                .weight(weight)
                .goalWeight(goalWeight)
                .ckdLevel(CkdLevel.LEVEL_1)
                .build();
        youngUser.updateOnboarding(youngOnboarding);
        
        User oldUser = createUser(Gender.MALE, 60);
        UserOnboarding oldOnboarding = UserOnboarding.builder()
                .user(oldUser)
                .activity(activity)
                .height(height)
                .weight(weight)
                .goalWeight(goalWeight)
                .ckdLevel(CkdLevel.LEVEL_1)
                .build();
        oldUser.updateOnboarding(oldOnboarding);

        // when
        int youngCalories = youngOnboarding.calculateDailyCalories();
        int oldCalories = oldOnboarding.calculateDailyCalories();

        // then
        assertThat(youngCalories).isGreaterThan(oldCalories);
        assertThat(youngCalories).isEqualTo(2633);
        assertThat(oldCalories).isEqualTo(2323);
    }
}