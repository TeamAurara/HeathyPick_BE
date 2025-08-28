package com.soongsil.eolala.health.persistence;

import com.soongsil.eolala.health.domain.UserHealthMetrics;
import com.soongsil.eolala.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserHealthMetricsRepository extends JpaRepository<UserHealthMetrics, Long> {

    Optional<UserHealthMetrics> findByUser(User user);
    

    Optional<UserHealthMetrics> findByUserId(Long userId);

    @Query("SELECT uhm FROM UserHealthMetrics uhm JOIN FETCH uhm.user WHERE uhm.userId = :userId")
    Optional<UserHealthMetrics> findByUserIdWithUser(@Param("userId") Long userId);
}