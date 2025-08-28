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
    
    @Builder
    public UserHealthMetrics(User user, Integer dailyCalories) {
        this.user = user;
        this.userId = user.getId();
        this.dailyCalories = dailyCalories;
    }
    
    public void updateMetrics(Integer dailyCalories) {
        this.dailyCalories = dailyCalories;
    }
}