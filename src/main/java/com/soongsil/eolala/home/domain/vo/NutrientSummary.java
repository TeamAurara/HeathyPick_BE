package com.soongsil.eolala.home.domain.vo;

import com.soongsil.eolala.food.domain.CustomFood;
import com.soongsil.eolala.food.domain.Food;
import com.soongsil.eolala.record.domain.Records;

import java.util.List;

import static com.soongsil.eolala.common.constants.NutritionConstants.NumericConstants.*;

public record NutrientSummary(
    double totalCalories,
    double totalCarbohydrate,
    double totalProtein,
    double totalFat,
    double totalSodium,
    double totalPotassium,
    double totalPhosphate
) {

    public static NutrientSummary from(List<Records> records) {
        double calories = 0;
        double carbohydrate = 0;
        double protein = 0;
        double fat = 0;
        double sodium = 0;
        double potassium = 0;
        double phosphate = 0;

        for (Records record : records) {
            if (record.getFood() != null) {
                Food food = record.getFood();
                calories += nullSafeDouble(food.getCalorie());
                carbohydrate += nullSafeDouble(food.getCarbohydrate());
                protein += nullSafeDouble(food.getProtein());
                fat += nullSafeDouble(food.getFat());
                sodium += nullSafeDouble(food.getSodium());
                potassium += nullSafeDouble(food.getPotassium());
                phosphate += nullSafeDouble(food.getPhosphate());
            } else if (record.getCustomFood() != null) {
                CustomFood customFood = record.getCustomFood();
                calories += nullSafeDouble(customFood.getCalorie());
                carbohydrate += nullSafeDouble(customFood.getCarbohydrate());
                protein += nullSafeDouble(customFood.getProtein());
                fat += nullSafeDouble(customFood.getFat());
            }
        }

        return new NutrientSummary(
            roundToTwoDecimals(calories),
            roundToTwoDecimals(carbohydrate),
            roundToTwoDecimals(protein),
            roundToTwoDecimals(fat),
            roundToTwoDecimals(sodium),
            roundToTwoDecimals(potassium),
            roundToTwoDecimals(phosphate)
        );
    }

    private static double nullSafeDouble(Double value) {
        return value != null ? value : DEFAULT_VALUE;
    }
    
    private static double roundToTwoDecimals(double value) {
        return Math.round(value * ROUNDING_SCALE) / ROUNDING_SCALE;
    }

    public int getRemainingCalories(int dailyCaloriesGoal) {
        int consumed = (int) totalCalories;
        int remaining = dailyCaloriesGoal - consumed;
        return Math.max(0, remaining);
    }
}