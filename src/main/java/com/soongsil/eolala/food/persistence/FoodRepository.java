package com.soongsil.eolala.food.persistence;

import com.soongsil.eolala.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findAllByMenuName(String menuName);
}
