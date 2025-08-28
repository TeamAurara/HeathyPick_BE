package com.soongsil.eolala.home.domain.vo;

import com.soongsil.eolala.food.domain.CustomFood;
import com.soongsil.eolala.food.domain.Food;
import com.soongsil.eolala.record.domain.Records;

import java.util.List;

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
            Math.round(calories * 100.0) / 100.0,
            Math.round(carbohydrate * 100.0) / 100.0,
            Math.round(protein * 100.0) / 100.0,
            Math.round(fat * 100.0) / 100.0,
            Math.round(sodium * 100.0) / 100.0,
            Math.round(potassium * 100.0) / 100.0,
            Math.round(phosphate * 100.0) / 100.0
        );
    }

    private static double nullSafeDouble(Double value) {
        return value != null ? value : 0.0;
    }

    public int getRemainingCalories(Integer dailyCaloriesGoal) {
        if (dailyCaloriesGoal == null) {
            return 0;
        }
        return Math.max(0, dailyCaloriesGoal - (int) totalCalories);
    }
}