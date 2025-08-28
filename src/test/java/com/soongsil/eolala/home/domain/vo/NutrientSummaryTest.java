package com.soongsil.eolala.home.domain.vo;

import com.soongsil.eolala.food.domain.CustomFood;
import com.soongsil.eolala.food.domain.Food;
import com.soongsil.eolala.record.domain.Records;
import com.soongsil.eolala.record.domain.type.IntakeTimeType;
import com.soongsil.eolala.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class NutrientSummaryTest {

    @Test
    @DisplayName("빈 기록 리스트에서 영양소 합계 계산")
    void from_EmptyRecords_ReturnsZeroValues() {
        // given
        List<Records> emptyRecords = Collections.emptyList();

        // when
        NutrientSummary summary = NutrientSummary.from(emptyRecords);

        // then
        assertThat(summary.totalCalories()).isEqualTo(0.0);
        assertThat(summary.totalCarbohydrate()).isEqualTo(0.0);
        assertThat(summary.totalProtein()).isEqualTo(0.0);
        assertThat(summary.totalFat()).isEqualTo(0.0);
        assertThat(summary.totalSodium()).isEqualTo(0.0);
        assertThat(summary.totalPotassium()).isEqualTo(0.0);
        assertThat(summary.totalPhosphate()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Food 기록에서 영양소 합계 계산")
    void from_FoodRecords_CalculatesCorrectly() {
        // given
        Food food1 = Food.builder()
            .calorie(200.456)
            .carbohydrate(30.789)
            .protein(25.123)
            .fat(10.456)
            .sodium(500.789)
            .potassium(300.123)
            .phosphate(150.456)
            .build();

        Food food2 = Food.builder()
            .calorie(150.234)
            .carbohydrate(20.456)
            .protein(15.789)
            .fat(5.234)
            .sodium(300.456)
            .potassium(200.789)
            .phosphate(100.234)
            .build();

        List<Records> records = Arrays.asList(
            createRecordWithFood(food1),
            createRecordWithFood(food2)
        );

        // when
        NutrientSummary summary = NutrientSummary.from(records);

        // then
        assertThat(summary.totalCalories()).isEqualTo(350.69);
        assertThat(summary.totalCarbohydrate()).isEqualTo(51.25);
        assertThat(summary.totalProtein()).isEqualTo(40.91);
        assertThat(summary.totalFat()).isEqualTo(15.69);
        assertThat(summary.totalSodium()).isEqualTo(801.25);
        assertThat(summary.totalPotassium()).isEqualTo(500.91);
        assertThat(summary.totalPhosphate()).isEqualTo(250.69);
    }

    @Test
    @DisplayName("CustomFood 기록에서 영양소 합계 계산")
    void from_CustomFoodRecords_CalculatesCorrectly() {
        // given
        CustomFood customFood1 = CustomFood.builder()
            .calorie(100.0)
            .carbohydrate(10.0)
            .protein(20.0)
            .fat(3.0)
            .build();

        CustomFood customFood2 = CustomFood.builder()
            .calorie(150.0)
            .carbohydrate(15.0)
            .protein(25.0)
            .fat(5.0)
            .build();

        List<Records> records = Arrays.asList(
            createRecordWithCustomFood(customFood1),
            createRecordWithCustomFood(customFood2)
        );

        // when
        NutrientSummary summary = NutrientSummary.from(records);

        // then
        assertThat(summary.totalCalories()).isEqualTo(250.0);
        assertThat(summary.totalCarbohydrate()).isEqualTo(25.0);
        assertThat(summary.totalProtein()).isEqualTo(45.0);
        assertThat(summary.totalFat()).isEqualTo(8.0);
        assertThat(summary.totalSodium()).isEqualTo(0.0);
        assertThat(summary.totalPotassium()).isEqualTo(0.0);
        assertThat(summary.totalPhosphate()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Food와 CustomFood 혼합 기록에서 영양소 합계 계산")
    void from_MixedRecords_CalculatesCorrectly() {
        // given
        Food food = Food.builder()
            .calorie(200.0)
            .carbohydrate(30.0)
            .protein(25.0)
            .fat(10.0)
            .sodium(500.0)
            .potassium(300.0)
            .phosphate(150.0)
            .build();

        CustomFood customFood = CustomFood.builder()
            .calorie(150.0)
            .carbohydrate(20.0)
            .protein(15.0)
            .fat(5.0)
            .build();

        List<Records> records = Arrays.asList(
            createRecordWithFood(food),
            createRecordWithCustomFood(customFood)
        );

        // when
        NutrientSummary summary = NutrientSummary.from(records);

        // then
        assertThat(summary.totalCalories()).isEqualTo(350.0);
        assertThat(summary.totalCarbohydrate()).isEqualTo(50.0);
        assertThat(summary.totalProtein()).isEqualTo(40.0);
        assertThat(summary.totalFat()).isEqualTo(15.0);
        assertThat(summary.totalSodium()).isEqualTo(500.0);
        assertThat(summary.totalPotassium()).isEqualTo(300.0);
        assertThat(summary.totalPhosphate()).isEqualTo(150.0);
    }

    @Test
    @DisplayName("null 값이 포함된 Food 처리")
    void from_RecordsWithNullValues_HandlesGracefully() {
        // given
        Food foodWithNulls = Food.builder()
            .calorie(100.0)
            .carbohydrate(null)
            .protein(20.0)
            .fat(null)
            .sodium(null)
            .potassium(50.0)
            .phosphate(null)
            .build();

        List<Records> records = List.of(createRecordWithFood(foodWithNulls));

        // when
        NutrientSummary summary = NutrientSummary.from(records);

        // then
        assertThat(summary.totalCalories()).isEqualTo(100.0);
        assertThat(summary.totalCarbohydrate()).isEqualTo(0.0);
        assertThat(summary.totalProtein()).isEqualTo(20.0);
        assertThat(summary.totalFat()).isEqualTo(0.0);
        assertThat(summary.totalSodium()).isEqualTo(0.0);
        assertThat(summary.totalPotassium()).isEqualTo(50.0);
        assertThat(summary.totalPhosphate()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("getRemainingCalories - 정상 계산")
    void getRemainingCalories_NormalCase() {
        // given
        NutrientSummary summary = new NutrientSummary(
            500.0, 50.0, 40.0, 20.0, 1000.0, 500.0, 200.0
        );

        // when
        int remaining = summary.getRemainingCalories(2000);

        // then
        assertThat(remaining).isEqualTo(1500); // 2000 - 500
    }

    @Test
    @DisplayName("getRemainingCalories - 섭취 칼로리가 목표를 초과한 경우 0 반환")
    void getRemainingCalories_ExceededCalories_ReturnsZero() {
        // given
        NutrientSummary summary = new NutrientSummary(
            2500.0, 250.0, 100.0, 80.0, 3000.0, 1500.0, 800.0
        );

        // when
        int remaining = summary.getRemainingCalories(2000);

        // then
        assertThat(remaining).isEqualTo(0); // 음수가 아닌 0 반환
    }


    @Test
    @DisplayName("소수점 반올림 정확도 테스트")
    void from_Records_RoundingPrecision() {
        // given
        Food food = Food.builder()
            .calorie(123.456789)
            .carbohydrate(45.123456)
            .protein(67.891234)
            .fat(12.345678)
            .sodium(234.567891)
            .potassium(456.789123)
            .phosphate(789.123456)
            .build();

        List<Records> records = List.of(createRecordWithFood(food));

        // when
        NutrientSummary summary = NutrientSummary.from(records);

        // then
        assertThat(summary.totalCalories()).isEqualTo(123.46);
        assertThat(summary.totalCarbohydrate()).isEqualTo(45.12);
        assertThat(summary.totalProtein()).isEqualTo(67.89);
        assertThat(summary.totalFat()).isEqualTo(12.35);
        assertThat(summary.totalSodium()).isEqualTo(234.57);
        assertThat(summary.totalPotassium()).isEqualTo(456.79);
        assertThat(summary.totalPhosphate()).isEqualTo(789.12);
    }

    private Records createRecordWithFood(Food food) {
        return Records.builder()
            .food(food)
            .intakeTimeType(IntakeTimeType.BREAKFAST)
            .user(mock(User.class))
            .build();
    }

    private Records createRecordWithCustomFood(CustomFood customFood) {
        return Records.builder()
            .customFood(customFood)
            .intakeTimeType(IntakeTimeType.LUNCH)
            .user(mock(User.class))
            .build();
    }
}