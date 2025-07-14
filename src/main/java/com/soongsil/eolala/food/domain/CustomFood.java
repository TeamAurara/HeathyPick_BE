package com.soongsil.eolala.food.domain;

import com.soongsil.eolala.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "custom_food_nutrients")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CustomFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Column(name = "carbohydrate", nullable = true)
    private Double carbohydrate; //탄수화물

    @Column(name = "protein", nullable = true)
    private Double protein; //단백질

    @Column(name = "fat", nullable = true)
    private Double fat; //지방

    @Column(name = "calorie", nullable = true)
    private Double calorie; //칼로리

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Builder
    public CustomFood(String menuName, Double carbohydrate, Double protein, Double fat, Double calorie, User user) {
        this.menuName = menuName;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
        this.calorie = calorie;
        this.user = user;
    }
}
