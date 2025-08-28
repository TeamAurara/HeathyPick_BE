package com.soongsil.eolala.user.domain;

import com.soongsil.eolala.global.domain.BaseEntity;
import com.soongsil.eolala.user.domain.type.Activity;
import com.soongsil.eolala.user.domain.type.CkdLevel;
import com.soongsil.eolala.user.domain.type.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user_onboarding")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserOnboarding extends BaseEntity {

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

    public int calculateDailyCalories() {
        double bmr;
        if (user.getGender() == Gender.MALE) {
            bmr = (10 * weight) + (6.25 * height) - (5 * user.getAge()) + 5;
        } else {
            bmr = (10 * weight) + (6.25 * height) - (5 * user.getAge()) - 161;
        }

        double activityMultiplier = getActivityMultiplier();
        double tdee = bmr * activityMultiplier;

        double calorieAdjustment = calculateCalorieAdjustment();
        double adjustedCalories = tdee + calorieAdjustment;

        if (user.getGender() == Gender.MALE) {
            adjustedCalories = Math.max(adjustedCalories, 1500);
        } else {
            adjustedCalories = Math.max(adjustedCalories, 1200);
        }

        return (int) Math.round(adjustedCalories);
    }

    private double getActivityMultiplier() {
        return switch (activity) {
            case MOVE_NONE -> 1.2;
            case MOVE_LESS -> 1.375;
            case MOVE_WELL -> 1.55;
            case MOVE_HARD -> 1.725;
        };
    }

    private double calculateCalorieAdjustment() {
        double weightDifference = goalWeight - weight;
        
        if (Math.abs(weightDifference) < 1) {
            return 0;
        } else if (weightDifference < 0) {
            return Math.max(-750, -500);
        } else {
            return Math.min(500, 300);
        }
    }
}
