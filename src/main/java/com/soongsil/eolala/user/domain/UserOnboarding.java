package com.soongsil.eolala.user.domain;

import com.soongsil.eolala.user.domain.type.Activity;
import com.soongsil.eolala.user.domain.type.CkdLevel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user_onboarding")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserOnboarding {

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
}
