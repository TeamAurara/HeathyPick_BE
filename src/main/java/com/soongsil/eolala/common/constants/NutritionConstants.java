package com.soongsil.eolala.common.constants;


public final class NutritionConstants {
    
    private NutritionConstants() {
		//인스턴스화 방지
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
    
    // CKD 단계별 영양소 제한 상수
    public static final class CkdNutrientLimits {
        // 단백질 제한 (g/kg 체중)
        public static final double PROTEIN_LEVEL_1_2 = 0.8;     // CKD 1-2단계
        public static final double PROTEIN_LEVEL_3 = 0.7;       // CKD 3a-3b단계
        public static final double PROTEIN_LEVEL_4_5 = 0.6;     // CKD 4-5단계 (투석 전)
        public static final double PROTEIN_DIALYSIS = 1.2;      // 투석 중
        
        // 나트륨 제한 (mg/일)
        public static final int SODIUM_LEVEL_1_2 = 2300;
        public static final int SODIUM_LEVEL_3_5 = 2000;
        
        // 칼륨 제한 (mg/일)
        public static final int POTASSIUM_NO_LIMIT = -1;        // 제한 없음
        public static final int POTASSIUM_LEVEL_3B_4 = 2500;
        public static final int POTASSIUM_LEVEL_5 = 2000;
        
        // 인 제한 (mg/일)
        public static final int PHOSPHATE_NO_LIMIT = -1;        // 제한 없음
        public static final int PHOSPHATE_LEVEL_3 = 900;
        public static final int PHOSPHATE_LEVEL_4_5 = 800;
        
        // 영양소 비율 (%)
        public static final double CARB_RATIO_MIN = 0.5;        // 50%
        public static final double CARB_RATIO_MAX = 0.6;        // 60%
        public static final double FAT_RATIO_MIN = 0.25;        // 25%
        public static final double FAT_RATIO_MAX = 0.35;        // 35%
        
        // 칼로리당 영양소 변환 계수
        public static final int CALORIES_PER_GRAM_CARB = 4;
        public static final int CALORIES_PER_GRAM_PROTEIN = 4;
        public static final int CALORIES_PER_GRAM_FAT = 9;
        
        private CkdNutrientLimits() {}
    }
    
    // 수치 처리 관련 상수
    public static final class NumericConstants {
        public static final double ROUNDING_SCALE = 100.0;  // 소수점 둘째 자리 반올림용
        public static final double DEFAULT_VALUE = 0.0;     // null 대체 기본값
        
        private NumericConstants() {}
    }
}