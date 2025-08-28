package com.soongsil.eolala.common.constants;


public final class NutritionConstants {
    
    private NutritionConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
    
    // 영양소 이름 상수
    public static final class NutrientNames {
        public static final String CARBOHYDRATE = "탄수화물";
        public static final String PROTEIN = "단백질";
        public static final String FAT = "지방";
        public static final String SODIUM = "나트륨";
        public static final String POTASSIUM = "칼륨";
        public static final String PHOSPHATE = "인";
        
        private NutrientNames() {}
    }
    
    // 영양소 단위 상수
    public static final class Units {
        public static final String GRAM = "g";
        public static final String MILLIGRAM = "mg";
        public static final String KILOCALORIE = "kcal";
        
        private Units() {}
    }
    
    // 칼로리 관련 상수
    public static final class CalorieConstants {
        // 최소 일일 권장 칼로리
        public static final int MALE_MINIMUM_CALORIES = 1500;
        public static final int FEMALE_MINIMUM_CALORIES = 1200;
        
        // 체중 조절 목표 칼로리 조정값
        public static final int WEIGHT_LOSS_DAILY_DEFICIT = -500;      // 주당 0.5kg 감량
        public static final int WEIGHT_LOSS_MAX_DEFICIT = -750;        // 최대 감량 제한
        public static final int WEIGHT_GAIN_DAILY_SURPLUS = 300;       // 주당 0.25kg 증량
        public static final int WEIGHT_GAIN_MAX_SURPLUS = 500;         // 최대 증량 제한
        
        // 체중 차이 임계값
        public static final double WEIGHT_MAINTENANCE_THRESHOLD = 1.0;  // kg
        
        private CalorieConstants() {}
    }
    
    // 수치 처리 관련 상수
    public static final class NumericConstants {
        public static final double ROUNDING_SCALE = 100.0;  // 소수점 둘째 자리 반올림용
        public static final double DEFAULT_VALUE = 0.0;     // null 대체 기본값
        
        private NumericConstants() {}
    }
}