package com.soongsil.eolala.record.application;

import com.soongsil.eolala.food.application.CustomFoodService;
import com.soongsil.eolala.food.application.FoodService;
import com.soongsil.eolala.food.domain.CustomFood;
import com.soongsil.eolala.record.domain.Records;
import com.soongsil.eolala.record.dto.request.RecordsCustomFoodRequest;
import com.soongsil.eolala.record.dto.request.RecordsFoodRequest;
import com.soongsil.eolala.record.dto.response.DailyRecordResponse;
import com.soongsil.eolala.record.exception.InvalidRequestException;
import com.soongsil.eolala.record.exception.RecordsErrorType;
import com.soongsil.eolala.record.persistence.RecordsRepository;
import com.soongsil.eolala.user.application.UserService;
import com.soongsil.eolala.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordsService {

    private final RecordsRepository recordsRepository;

    private final FoodService foodService;
    private final UserService userService;
    private final CustomFoodService customFoodService;

    @Transactional
    public void saveFoodRecord(Long userId, RecordsFoodRequest recordsFoodRequest) {
        User user = userService.getUser(userId);

        Optional<Long> foodId = Optional.ofNullable(recordsFoodRequest.foodId());
        Optional<Long> customFoodId = Optional.ofNullable(recordsFoodRequest.customFoodId());

        boolean hasFoodId = foodId.isPresent();
        boolean hasCustomFoodId = customFoodId.isPresent();

        if (hasFoodId == hasCustomFoodId) {
            throw new InvalidRequestException(RecordsErrorType.ONLY_ONE_FOOD_ID_ALLOWED);
        }

        if (hasFoodId) {
            saveSearchedFood(user, foodId, recordsFoodRequest);
        }

        if (hasCustomFoodId) {
            saveSearchedCustomFood(user, customFoodId, recordsFoodRequest);
        }
    }

    @Transactional
    public void saveCustomFoodRecord(Long userId, RecordsCustomFoodRequest recordsCustomFoodRequest) {
        User user = userService.getUser(userId);
        CustomFood customFood = customFoodService.saveCustomFoodRecord(user, recordsCustomFoodRequest);
        recordsRepository.save(
                Records.builder()
                        .user(user)
                        .customFood(customFood)
                        .intakeTimeType(recordsCustomFoodRequest.intakeTimeType())
                        .build()
        );
    }

    private void saveSearchedFood(User user, Optional<Long> foodId, RecordsFoodRequest recordsFoodRequest) {
        foodId.flatMap(foodService::getFood)
                .ifPresent(food -> {
                    recordsRepository.save(
                            Records.builder()
                                    .user(user)
                                    .food(food)
                                    .intakeTimeType(recordsFoodRequest.intakeTimeType())
                                    .build()
                    );
                });
    }

    private void saveSearchedCustomFood(User user, Optional<Long> customFoodId, RecordsFoodRequest recordsFoodRequest) {
        customFoodId.flatMap(customFoodService::getCustomFood)
                .ifPresent(customFood -> {
                    recordsRepository.save(
                            Records.builder()
                                    .user(user)
                                    .customFood(customFood)
                                    .intakeTimeType(recordsFoodRequest.intakeTimeType())
                                    .build()
                    );
                });
    }

    @Transactional(readOnly = true)
    public DailyRecordResponse getDailyRecords(Long userId, LocalDate date) {
        User user = userService.getUser(userId);

        List<Records> recordsList = recordsRepository.findByUserAndCreatedDate(user, date);

        return calculateTotal(recordsList, date);
    }

    private DailyRecordResponse calculateTotal(List<Records> recordsList, LocalDate todayDate) {
        double totalBreakfast = 0;
        double totalLunch = 0;
        double totalDinner = 0;
        double totalWater = 0;

        for (Records record : recordsList) {
            double calorie = record.getFood() != null ? record.getFood().getCalorie() : 0;

            switch (record.getIntakeTimeType()) {
                case BREAKFAST -> totalBreakfast += calorie;
                case LUNCH -> totalLunch += calorie;
                case DINNER -> totalDinner += calorie;
                case WATER -> totalWater += record.getWater();
            }
        }

        return DailyRecordResponse.of(
                todayDate,
                totalBreakfast,
                totalLunch,
                totalDinner,
                totalWater
        );
    }
}
