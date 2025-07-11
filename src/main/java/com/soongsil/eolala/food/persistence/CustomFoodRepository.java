package com.soongsil.eolala.food.persistence;

import com.soongsil.eolala.food.domain.CustomFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomFoodRepository extends JpaRepository<CustomFood, Long> {

}
