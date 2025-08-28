package com.soongsil.eolala.health.domain;

import com.soongsil.eolala.global.domain.BaseEntity;
import com.soongsil.eolala.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user_health_metrics")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserHealthMetrics extends BaseEntity {
    
    @Id
    @Column(name = "user_id")
    private Long userId;
    
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "daily_calories", nullable = false)
    private Integer dailyCalories;
    
    @Column(name = "daily_carbohydrate")
    private Integer dailyCarbohydrate;
    
    @Column(name = "daily_protein")
    private Integer dailyProtein;
    
    @Column(name = "daily_fat")
    private Integer dailyFat;
    
    @Column(name = "daily_sodium")
    private Integer dailySodium;
    
    @Column(name = "daily_potassium")
    private Integer dailyPotassium;
    
    @Column(name = "daily_phosphate")
    private Integer dailyPhosphate;
    
    @Builder
    public UserHealthMetrics(User user, Integer dailyCalories, Integer dailyCarbohydrate,
                            Integer dailyProtein, Integer dailyFat, Integer dailySodium,
                            Integer dailyPotassium, Integer dailyPhosphate) {
        this.user = user;
        this.userId = user.getId();
        this.dailyCalories = dailyCalories;
        this.dailyCarbohydrate = dailyCarbohydrate;
        this.dailyProtein = dailyProtein;
        this.dailyFat = dailyFat;
        this.dailySodium = dailySodium;
        this.dailyPotassium = dailyPotassium;
        this.dailyPhosphate = dailyPhosphate;
    }
    
    public void updateMetrics(Integer dailyCalories, Integer dailyCarbohydrate,
                             Integer dailyProtein, Integer dailyFat, Integer dailySodium,
                             Integer dailyPotassium, Integer dailyPhosphate) {
        this.dailyCalories = dailyCalories;
        this.dailyCarbohydrate = dailyCarbohydrate;
        this.dailyProtein = dailyProtein;
        this.dailyFat = dailyFat;
        this.dailySodium = dailySodium;
        this.dailyPotassium = dailyPotassium;
        this.dailyPhosphate = dailyPhosphate;
    }

    public boolean hasPotassiumLimit() {
        return dailyPotassium != null && dailyPotassium > 0;
    }
    
    public boolean hasPhosphateLimit() {
        return dailyPhosphate != null && dailyPhosphate > 0;
    }
}