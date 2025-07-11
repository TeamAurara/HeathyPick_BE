package com.soongsil.eolala.record.domain;

import com.soongsil.eolala.food.domain.CustomFood;
import com.soongsil.eolala.food.domain.Food;
import com.soongsil.eolala.record.domain.type.IntakeTimeType;
import com.soongsil.eolala.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user_records")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Records {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "water", nullable = true)
    private double water;   //물 섭취량

    @Column(name = "intake_time_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private IntakeTimeType intakeTimeType; // 섭취 시간 타입

    @JoinColumn(name = "food_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Food food;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @JoinColumn(name = "custom_food_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private CustomFood customFood;

    @Builder
    public Records(double water, IntakeTimeType intakeTimeType, Food food, User user, CustomFood customFood) {
        this.water = water;
        this.intakeTimeType = intakeTimeType;
        this.food = food;
        this.user = user;
        this.customFood = customFood;
    }
}

