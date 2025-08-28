package com.soongsil.eolala.user.domain;

import com.soongsil.eolala.common.constants.NutritionConstants;
import com.soongsil.eolala.global.domain.BaseEntity;
import com.soongsil.eolala.user.domain.type.Activity;
import com.soongsil.eolala.user.domain.type.CkdLevel;
import com.soongsil.eolala.user.domain.type.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.soongsil.eolala.common.constants.NutritionConstants.CalorieConstants.*;

@Table(name = "user_onboarding")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserOnboarding extends BaseEntity {
    
    // Mifflin-St Jeor 공식 상수
    private static final double BMR_WEIGHT_MULTIPLIER = 10.0;
    private static final double BMR_HEIGHT_MULTIPLIER = 6.25;
    private static final double BMR_AGE_MULTIPLIER = 5.0;
    private static final int BMR_MALE_CONSTANT = 5;
    private static final int BMR_FEMALE_CONSTANT = -161;
    
    // 활동 계수 상수
    private static final double ACTIVITY_MULTIPLIER_SEDENTARY = 1.2;      // 좌식 생활
    private static final double ACTIVITY_MULTIPLIER_LIGHT = 1.375;        // 가벼운 활동
    private static final double ACTIVITY_MULTIPLIER_MODERATE = 1.55;      // 보통 활동  
    private static final double ACTIVITY_MULTIPLIER_VERY_ACTIVE = 1.725;  // 매우 활동적

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity", nullable = false)
    private Activity activity;

    @Column(name = "height", nullable = false)
    private double height;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "goal_weight", nullable = false)
    private double goalWeight;

    @Enumerated(EnumType.STRING)
    @Column(name = "ckd_level", nullable = false)
    private CkdLevel ckdLevel;

    @Builder
    public UserOnboarding(Activity activity, double height, double weight, double goalWeight, CkdLevel ckdLevel, User user) {
        this.activity = activity;
        this.height = height;
        this.weight = weight;
        this.goalWeight = goalWeight;
        this.ckdLevel = ckdLevel;
        this.user = user;
    }

    /**
     * 일일 적정 칼로리를 계산합니다.
     * 별도 테이블(UserHealthMetrics)에 저장하기 위해 계산만 수행합니다.
     */
    public int calculateDailyCalories() {
        double bmr = calculateBMR();
        double activityMultiplier = getActivityMultiplier();
        double tdee = bmr * activityMultiplier;

        double calorieAdjustment = calculateCalorieAdjustment();
        double adjustedCalories = tdee + calorieAdjustment;

        adjustedCalories = ensureMinimumCalories(adjustedCalories);
        return (int) Math.round(adjustedCalories);
    }
    
    private double calculateBMR() {
        double bmr = (BMR_WEIGHT_MULTIPLIER * weight) + 
                     (BMR_HEIGHT_MULTIPLIER * height) - 
                     (BMR_AGE_MULTIPLIER * user.getAge());
        
        if (user.getGender() == Gender.MALE) {
            bmr += BMR_MALE_CONSTANT;
        } else {
            bmr += BMR_FEMALE_CONSTANT;
        }
        
        return bmr;
    }

    private double getActivityMultiplier() {
        return switch (activity) {
            case MOVE_NONE -> ACTIVITY_MULTIPLIER_SEDENTARY;
            case MOVE_LESS -> ACTIVITY_MULTIPLIER_LIGHT;
            case MOVE_WELL -> ACTIVITY_MULTIPLIER_MODERATE;
            case MOVE_HARD -> ACTIVITY_MULTIPLIER_VERY_ACTIVE;
        };
    }

    private double calculateCalorieAdjustment() {
        double weightDifference = goalWeight - weight;
        
        if (Math.abs(weightDifference) < WEIGHT_MAINTENANCE_THRESHOLD) {
            return 0;
        } else if (weightDifference < 0) {
            return Math.max(WEIGHT_LOSS_MAX_DEFICIT, WEIGHT_LOSS_DAILY_DEFICIT);
        } else {
            return Math.min(WEIGHT_GAIN_MAX_SURPLUS, WEIGHT_GAIN_DAILY_SURPLUS);
        }
    }
    
    private double ensureMinimumCalories(double calories) {
        int minimumCalories = (user.getGender() == Gender.MALE) ? 
                              MALE_MINIMUM_CALORIES : FEMALE_MINIMUM_CALORIES;
        return Math.max(calories, minimumCalories);
    }
}
