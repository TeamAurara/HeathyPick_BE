package com.soongsil.eolala.user.domain;

import com.soongsil.eolala.user.domain.type.Activity;
import com.soongsil.eolala.user.domain.type.CkdLevel;
import com.soongsil.eolala.user.domain.type.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class UserOnboardingNutrientTest {
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .email("test@test.com")
            .nickname("테스트유저")
            .gender(Gender.MALE)
            .age(35)
            .build();
    }
    
    @Nested
    @DisplayName("단백질 제한량 계산")
    class ProteinLimitTest {
        
        @Test
        @DisplayName("CKD 1-2단계: 체중 kg당 0.8g")
        void calculateProteinLimit_CKD_Level1_2() {
            // given
            UserOnboarding onboarding = UserOnboarding.builder()
                .user(testUser)
                .height(175)
                .weight(70)
                .goalWeight(70)
                .activity(Activity.MOVE_WELL)
                .ckdLevel(CkdLevel.LEVEL_1)
                .build();
            
            // when
            int proteinLimit = onboarding.calculateDailyProteinLimit();
            
            // then
            assertThat(proteinLimit).isEqualTo(56); // 70kg * 0.8 = 56g
        }
        
        @Test
        @DisplayName("CKD 3단계: 체중 kg당 0.7g")
        void calculateProteinLimit_CKD_Level3() {
            // given
            UserOnboarding onboarding = UserOnboarding.builder()
                .user(testUser)
                .height(175)
                .weight(70)
                .goalWeight(70)
                .activity(Activity.MOVE_WELL)
                .ckdLevel(CkdLevel.LEVEL_3A)
                .build();
            
            // when
            int proteinLimit = onboarding.calculateDailyProteinLimit();
            
            // then
            assertThat(proteinLimit).isEqualTo(49); // 70kg * 0.7 = 49g
        }
        
        @Test
        @DisplayName("CKD 4-5단계: 체중 kg당 0.6g")
        void calculateProteinLimit_CKD_Level4_5() {
            // given
            UserOnboarding onboarding = UserOnboarding.builder()
                .user(testUser)
                .height(175)
                .weight(70)
                .goalWeight(70)
                .activity(Activity.MOVE_WELL)
                .ckdLevel(CkdLevel.LEVEL_5)
                .build();
            
            // when
            int proteinLimit = onboarding.calculateDailyProteinLimit();
            
            // then
            assertThat(proteinLimit).isEqualTo(42); // 70kg * 0.6 = 42g
        }
    }
    
    @Nested
    @DisplayName("나트륨 제한량 계산")
    class SodiumLimitTest {
        
        @Test
        @DisplayName("CKD 1-2단계: 2300mg")
        void calculateSodiumLimit_CKD_Level1_2() {
            // given
            UserOnboarding onboarding = UserOnboarding.builder()
                .user(testUser)
                .height(175)
                .weight(70)
                .goalWeight(70)
                .activity(Activity.MOVE_WELL)
                .ckdLevel(CkdLevel.LEVEL_2)
                .build();
            
            // when
            int sodiumLimit = onboarding.calculateDailySodiumLimit();
            
            // then
            assertThat(sodiumLimit).isEqualTo(2300);
        }
        
        @Test
        @DisplayName("CKD 3-5단계: 2000mg")
        void calculateSodiumLimit_CKD_Level3_5() {
            // given
            UserOnboarding onboarding = UserOnboarding.builder()
                .user(testUser)
                .height(175)
                .weight(70)
                .goalWeight(70)
                .activity(Activity.MOVE_WELL)
                .ckdLevel(CkdLevel.LEVEL_4)
                .build();
            
            // when
            int sodiumLimit = onboarding.calculateDailySodiumLimit();
            
            // then
            assertThat(sodiumLimit).isEqualTo(2000);
        }
    }
    
    @Nested
    @DisplayName("칼륨 제한량 계산")
    class PotassiumLimitTest {
        
        @Test
        @DisplayName("CKD 1-3A단계: 제한 없음")
        void calculatePotassiumLimit_NoLimit() {
            // given
            UserOnboarding onboarding = UserOnboarding.builder()
                .user(testUser)
                .height(175)
                .weight(70)
                .goalWeight(70)
                .activity(Activity.MOVE_WELL)
                .ckdLevel(CkdLevel.LEVEL_3A)
                .build();
            
            // when
            int potassiumLimit = onboarding.calculateDailyPotassiumLimit();
            
            // then
            assertThat(potassiumLimit).isEqualTo(-1); // 제한 없음
        }
        
        @Test
        @DisplayName("CKD 3B-4단계: 2500mg")
        void calculatePotassiumLimit_Level3B_4() {
            // given
            UserOnboarding onboarding = UserOnboarding.builder()
                .user(testUser)
                .height(175)
                .weight(70)
                .goalWeight(70)
                .activity(Activity.MOVE_WELL)
                .ckdLevel(CkdLevel.LEVEL_3B)
                .build();
            
            // when
            int potassiumLimit = onboarding.calculateDailyPotassiumLimit();
            
            // then
            assertThat(potassiumLimit).isEqualTo(2500);
        }
        
        @Test
        @DisplayName("CKD 5단계: 2000mg")
        void calculatePotassiumLimit_Level5() {
            // given
            UserOnboarding onboarding = UserOnboarding.builder()
                .user(testUser)
                .height(175)
                .weight(70)
                .goalWeight(70)
                .activity(Activity.MOVE_WELL)
                .ckdLevel(CkdLevel.LEVEL_5)
                .build();
            
            // when
            int potassiumLimit = onboarding.calculateDailyPotassiumLimit();
            
            // then
            assertThat(potassiumLimit).isEqualTo(2000);
        }
    }
    
    @Nested
    @DisplayName("인 제한량 계산")
    class PhosphateLimitTest {
        
        @Test
        @DisplayName("CKD 1-2단계: 제한 없음")
        void calculatePhosphateLimit_NoLimit() {
            // given
            UserOnboarding onboarding = UserOnboarding.builder()
                .user(testUser)
                .height(175)
                .weight(70)
                .goalWeight(70)
                .activity(Activity.MOVE_WELL)
                .ckdLevel(CkdLevel.LEVEL_1)
                .build();
            
            // when
            int phosphateLimit = onboarding.calculateDailyPhosphateLimit();
            
            // then
            assertThat(phosphateLimit).isEqualTo(-1); // 제한 없음
        }
        
        @Test
        @DisplayName("CKD 3단계: 900mg")
        void calculatePhosphateLimit_Level3() {
            // given
            UserOnboarding onboarding = UserOnboarding.builder()
                .user(testUser)
                .height(175)
                .weight(70)
                .goalWeight(70)
                .activity(Activity.MOVE_WELL)
                .ckdLevel(CkdLevel.LEVEL_3B)
                .build();
            
            // when
            int phosphateLimit = onboarding.calculateDailyPhosphateLimit();
            
            // then
            assertThat(phosphateLimit).isEqualTo(900);
        }
        
        @Test
        @DisplayName("CKD 4-5단계: 800mg")
        void calculatePhosphateLimit_Level4_5() {
            // given
            UserOnboarding onboarding = UserOnboarding.builder()
                .user(testUser)
                .height(175)
                .weight(70)
                .goalWeight(70)
                .activity(Activity.MOVE_WELL)
                .ckdLevel(CkdLevel.LEVEL_5)
                .build();
            
            // when
            int phosphateLimit = onboarding.calculateDailyPhosphateLimit();
            
            // then
            assertThat(phosphateLimit).isEqualTo(800);
        }
    }
    
    @Nested
    @DisplayName("탄수화물/지방 권장량 계산")
    class MacroNutrientTest {
        
        @Test
        @DisplayName("탄수화물: 총 칼로리의 55%")
        void calculateCarbohydrateLimit() {
            // given
            UserOnboarding onboarding = UserOnboarding.builder()
                .user(testUser)
                .height(175)
                .weight(70)
                .goalWeight(70)
                .activity(Activity.MOVE_WELL)
                .ckdLevel(CkdLevel.LEVEL_3A)
                .build();
            
            // when
            int carbLimit = onboarding.calculateDailyCarbohydrateLimit();
            int dailyCalories = onboarding.calculateDailyCalories();
            
            // then
            // 총 칼로리의 약 55% (50-60%의 중간값)를 탄수화물로
            int expectedCarb = (int)Math.round(dailyCalories * 0.55 / 4);
            assertThat(carbLimit).isCloseTo(expectedCarb, within(5));
        }
        
        @Test
        @DisplayName("지방: 총 칼로리의 30%")
        void calculateFatLimit() {
            // given
            UserOnboarding onboarding = UserOnboarding.builder()
                .user(testUser)
                .height(175)
                .weight(70)
                .goalWeight(70)
                .activity(Activity.MOVE_WELL)
                .ckdLevel(CkdLevel.LEVEL_3A)
                .build();
            
            // when
            int fatLimit = onboarding.calculateDailyFatLimit();
            int dailyCalories = onboarding.calculateDailyCalories();
            
            // then
            // 총 칼로리의 약 30% (25-35%의 중간값)를 지방으로
            int expectedFat = (int)Math.round(dailyCalories * 0.30 / 9);
            assertThat(fatLimit).isCloseTo(expectedFat, within(5));
        }
    }
    
    private static org.assertj.core.data.Offset<Integer> within(int offset) {
        return org.assertj.core.data.Offset.offset(offset);
    }
}