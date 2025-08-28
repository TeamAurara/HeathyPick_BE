package com.soongsil.eolala.home.application;

import com.soongsil.eolala.food.domain.CustomFood;
import com.soongsil.eolala.food.domain.Food;
import com.soongsil.eolala.home.dto.response.HomeResponse;
import com.soongsil.eolala.record.domain.Records;
import com.soongsil.eolala.record.domain.type.IntakeTimeType;
import com.soongsil.eolala.record.persistence.RecordsRepository;
import com.soongsil.eolala.user.domain.User;
import com.soongsil.eolala.user.domain.UserOnboarding;
import com.soongsil.eolala.user.domain.type.Activity;
import com.soongsil.eolala.user.domain.type.CkdLevel;
import com.soongsil.eolala.user.domain.type.Gender;
import com.soongsil.eolala.user.domain.type.Role;
import com.soongsil.eolala.user.domain.type.SocialType;
import com.soongsil.eolala.user.exception.UserErrorType;
import com.soongsil.eolala.user.exception.UserNotFoundException;
import com.soongsil.eolala.user.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HomeServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecordsRepository recordsRepository;

    @InjectMocks
    private HomeService homeService;

    private User user;
    private Food food;
    private CustomFood customFood;

    @BeforeEach
    void setUp() {
        user = createUserWithOnboarding();
        user.updateDailyCalories(2000);
        
        food = Food.builder()
            .menuName("닭가슴살")
            .calorie(200.0)
            .carbohydrate(5.0)
            .protein(40.0)
            .fat(3.0)
            .sodium(100.0)
            .potassium(350.0)
            .phosphate(200.0)
            .build();
            
        customFood = CustomFood.builder()
            .menuName("프로틴 쉐이크")
            .calorie(150.0)
            .carbohydrate(10.0)
            .protein(25.0)
            .fat(2.0)
            .user(user)
            .build();
    }

    @Test
    @DisplayName("홈 화면 조회 성공 - 음식 기록이 있는 경우")
    void getHomeSummary_WithFoodRecords_Success() {
        // given
        Long userId = 1L;
        List<Records> records = Arrays.asList(
            Records.builder()
                .food(food)
                .intakeTimeType(IntakeTimeType.BREAKFAST)
                .user(user)
                .build(),
            Records.builder()
                .customFood(customFood)
                .intakeTimeType(IntakeTimeType.LUNCH)
                .user(user)
                .build()
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(recordsRepository.findByUserAndCreatedDate(eq(user), any(LocalDate.class)))
            .thenReturn(records);

        // when
        HomeResponse response = homeService.getHomeSummary(userId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.calories()).isNotNull();
        assertThat(response.calories().dailyGoal()).isEqualTo(2000);
        assertThat(response.calories().consumed()).isEqualTo(350); // 200 + 150
        assertThat(response.calories().remainingCalories()).isEqualTo(1650); // 2000 - 350

        assertThat(response.nutrients()).isNotNull();
        assertThat(response.nutrients().carbohydrate().amount()).isEqualTo(15.0); // 5 + 10
        assertThat(response.nutrients().protein().amount()).isEqualTo(65.0); // 40 + 25
        assertThat(response.nutrients().fat().amount()).isEqualTo(5.0); // 3 + 2
        assertThat(response.nutrients().sodium().amount()).isEqualTo(100.0);
        assertThat(response.nutrients().potassium().amount()).isEqualTo(350.0);
        assertThat(response.nutrients().phosphate().amount()).isEqualTo(200.0);
    }

    @Test
    @DisplayName("홈 화면 조회 성공 - 음식 기록이 없는 경우")
    void getHomeSummary_WithoutFoodRecords_Success() {
        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(recordsRepository.findByUserAndCreatedDate(eq(user), any(LocalDate.class)))
            .thenReturn(Collections.emptyList());

        // when
        HomeResponse response = homeService.getHomeSummary(userId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.calories().dailyGoal()).isEqualTo(2000);
        assertThat(response.calories().consumed()).isEqualTo(0);
        assertThat(response.calories().remainingCalories()).isEqualTo(2000);

        assertThat(response.nutrients().carbohydrate().amount()).isEqualTo(0.0);
        assertThat(response.nutrients().protein().amount()).isEqualTo(0.0);
        assertThat(response.nutrients().fat().amount()).isEqualTo(0.0);
        assertThat(response.nutrients().sodium().amount()).isEqualTo(0.0);
        assertThat(response.nutrients().potassium().amount()).isEqualTo(0.0);
        assertThat(response.nutrients().phosphate().amount()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("홈 화면 조회 성공 - 일일 칼로리가 설정되지 않은 경우")
    void getHomeSummary_WithoutDailyCalories_Success() {
        // given
        Long userId = 1L;
        User userWithoutCalories = createUserWithOnboarding();
        // dailyCalories를 설정하지 않음 (null 상태)
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(userWithoutCalories));
        when(recordsRepository.findByUserAndCreatedDate(eq(userWithoutCalories), any(LocalDate.class)))
            .thenReturn(Collections.emptyList());

        // when
        HomeResponse response = homeService.getHomeSummary(userId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.calories().dailyGoal()).isEqualTo(0);
        assertThat(response.calories().remainingCalories()).isEqualTo(0);
    }

    @Test
    @DisplayName("홈 화면 조회 실패 - 유저를 찾을 수 없음")
    void getHomeSummary_UserNotFound_ThrowsException() {
        // given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> homeService.getHomeSummary(userId))
            .isInstanceOf(UserNotFoundException.class)
            .hasFieldOrPropertyWithValue("errorType", UserErrorType.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("홈 화면 조회 실패 - 온보딩이 완료되지 않은 유저")
    void getHomeSummary_UserNotOnboarded_ThrowsException() {
        // given
        Long userId = 1L;
        User notOnboardedUser = createUserWithoutOnboarding();
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(notOnboardedUser));

        // when & then
        assertThatThrownBy(() -> homeService.getHomeSummary(userId))
            .isInstanceOf(UserNotFoundException.class)
            .hasFieldOrPropertyWithValue("errorType", UserErrorType.USER_NOT_ONBOARDED);
    }

    @Test
    @DisplayName("홈 화면 조회 - 섭취 칼로리가 목표 칼로리를 초과한 경우")
    void getHomeSummary_ExceededCalories_ReturnsZero() {
        // given
        Long userId = 1L;
        user.updateDailyCalories(300); // 목표 칼로리를 낮게 설정
        
        List<Records> records = Arrays.asList(
            Records.builder()
                .food(food) // 200 칼로리
                .intakeTimeType(IntakeTimeType.BREAKFAST)
                .user(user)
                .build(),
            Records.builder()
                .customFood(customFood) // 150 칼로리
                .intakeTimeType(IntakeTimeType.LUNCH)
                .user(user)
                .build()
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(recordsRepository.findByUserAndCreatedDate(eq(user), any(LocalDate.class)))
            .thenReturn(records);

        // when
        HomeResponse response = homeService.getHomeSummary(userId);

        // then
        assertThat(response.calories().consumed()).isEqualTo(350);
        assertThat(response.calories().dailyGoal()).isEqualTo(300);
        assertThat(response.calories().remainingCalories()).isEqualTo(0); // 음수가 아닌 0 반환
    }

    @Test
    @DisplayName("홈 화면 조회 - null 값 처리 확인")
    void getHomeSummary_WithNullNutrients_HandlesGracefully() {
        // given
        Long userId = 1L;
        Food foodWithNulls = Food.builder()
            .menuName("테스트 음식")
            .calorie(100.0)
            .carbohydrate(null)
            .protein(null)
            .fat(10.0)
            .sodium(null)
            .potassium(50.0)
            .phosphate(null)
            .build();

        Records record = Records.builder()
            .food(foodWithNulls)
            .intakeTimeType(IntakeTimeType.DINNER)
            .user(user)
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(recordsRepository.findByUserAndCreatedDate(eq(user), any(LocalDate.class)))
            .thenReturn(List.of(record));

        // when
        HomeResponse response = homeService.getHomeSummary(userId);

        // then
        assertThat(response.calories().consumed()).isEqualTo(100);
        assertThat(response.nutrients().carbohydrate().amount()).isEqualTo(0.0);
        assertThat(response.nutrients().protein().amount()).isEqualTo(0.0);
        assertThat(response.nutrients().fat().amount()).isEqualTo(10.0);
        assertThat(response.nutrients().sodium().amount()).isEqualTo(0.0);
        assertThat(response.nutrients().potassium().amount()).isEqualTo(50.0);
        assertThat(response.nutrients().phosphate().amount()).isEqualTo(0.0);
    }

    private User createUserWithOnboarding() {
        User user = User.builder()
            .email("test@test.com")
            .providerId("12345")
            .socialType(SocialType.KAKAO)
            .nickname("테스터")
            .gender(Gender.MALE)
            .age(30)
            .profileImageUrl("profile.jpg")
            .isOnboarded(true)
            .role(Role.USER)
            .build();

        UserOnboarding onboarding = UserOnboarding.builder()
            .user(user)
            .activity(Activity.MOVE_WELL)
            .height(175)
            .weight(70)
            .goalWeight(68)
            .ckdLevel(CkdLevel.LEVEL_1)
            .build();
        
        user.updateOnboarding(onboarding);
        return user;
    }

    private User createUserWithoutOnboarding() {
        return User.builder()
            .email("test@test.com")
            .providerId("12345")
            .socialType(SocialType.KAKAO)
            .nickname("테스터")
            .gender(Gender.MALE)
            .age(30)
            .profileImageUrl("profile.jpg")
            .isOnboarded(false)
            .role(Role.USER)
            .build();
    }
}