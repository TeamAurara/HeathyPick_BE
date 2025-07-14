package com.soongsil.eolala.food.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "food_nutrients")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Food {

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

    @Column(name = "sodium", nullable = true)
    private Double sodium;  //나트륨

    @Column(name = "potassium", nullable = true)
    private Double potassium;   //칼륨

    @Column(name = "phosphate", nullable = true)
    private Double phosphate;   //인


    @Builder
    public Food(String menuName, Double carbohydrate, Double protein, Double fat, Double calorie, Double sodium, Double potassium, Double phosphate) {
        this.menuName = menuName;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
        this.calorie = calorie;
        this.sodium = sodium;
        this.potassium = potassium;
        this.phosphate = phosphate;
    }
}
