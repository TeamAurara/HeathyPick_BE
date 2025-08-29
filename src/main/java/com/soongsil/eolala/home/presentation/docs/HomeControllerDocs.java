package com.soongsil.eolala.home.presentation.docs;

import com.soongsil.eolala.global.support.dto.response.ApiResponse;
import com.soongsil.eolala.home.dto.response.HomeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "홈 화면", description = "CKD 환자를 위한 일일 영양 관리 홈 화면 API")
public interface HomeControllerDocs {

    @Operation(
        summary = "홈 화면 정보 조회",
        description = """
            CKD 환자의 일일 영양 관리를 위한 종합 정보를 제공합니다.
            
            ### 제공 정보:
            
            #### 1. 칼로리 정보
            - **remainingCalories**: 오늘 남은 섭취 가능 칼로리
            - **dailyGoal**: 일일 목표 칼로리 (체중, 활동량 기반 계산)
            - **consumed**: 오늘 섭취한 칼로리
            
            #### 2. 영양소별 섭취 현황 및 CKD 단계별 제한량
            - **탄수화물**: 섭취량(g) / 일일 권장량 (칼로리의 55%)
            - **단백질**: 섭취량(g) / CKD 단계별 제한량
              - CKD 1-2: 0.8g/kg
              - CKD 3: 0.7g/kg
              - CKD 4-5: 0.6g/kg
            - **지방**: 섭취량(g) / 일일 권장량 (칼로리의 30%)
            - **나트륨**: 섭취량(mg) / CKD 단계별 제한량
              - CKD 1-2: 2300mg
              - CKD 3-5: 2000mg
            - **칼륨**: 섭취량(mg) / CKD 단계별 제한량
              - CKD 1-3A: 제한 없음
              - CKD 3B-4: 2500mg
              - CKD 5: 2000mg
            - **인**: 섭취량(mg) / CKD 단계별 제한량
              - CKD 1-2: 제한 없음
              - CKD 3: 900mg
              - CKD 4-5: 800mg
            
            각 영양소별로 남은 허용량도 함께 제공됩니다.
            """
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "홈 화면 정보 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HomeResponse.class),
                examples = @ExampleObject(
                    name = "CKD 3단계 환자 예시",
                    value = """
                        {
                          "success": true,
                          "data": {
                            "calories": {
                              "remainingCalories": 1200,
                              "dailyGoal": 2000,
                              "consumed": 800
                            },
                            "nutrients": {
                              "carbohydrate": {
                                "name": "탄수화물",
                                "amount": 100.5,
                                "dailyLimit": 275,
                                "unit": "g"
                              },
                              "protein": {
                                "name": "단백질",
                                "amount": 25.3,
                                "dailyLimit": 49,
                                "unit": "g"
                              },
                              "fat": {
                                "name": "지방",
                                "amount": 15.2,
                                "dailyLimit": 67,
                                "unit": "g"
                              },
                              "sodium": {
                                "name": "나트륨",
                                "amount": 800,
                                "dailyLimit": 2000,
                                "unit": "mg"
                              },
                              "potassium": {
                                "name": "칼륨",
                                "amount": 1200,
                                "dailyLimit": 2500,
                                "unit": "mg"
                              },
                              "phosphate": {
                                "name": "인",
                                "amount": 400,
                                "dailyLimit": 900,
                                "unit": "mg"
                              }
                            }
                          }
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없거나 온보딩이 완료되지 않음",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "success": false,
                          "error": {
                            "code": "USER_NOT_FOUND",
                            "message": "사용자를 찾을 수 없습니다."
                          }
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "success": false,
                          "error": {
                            "code": "UNAUTHORIZED",
                            "message": "인증이 필요합니다."
                          }
                        }
                        """
                )
            )
        )
    })
    ApiResponse<HomeResponse> getHomeSummary(
        @Parameter(description = "사용자 ID", required = true, example = "1")
        Long userId
    );
}