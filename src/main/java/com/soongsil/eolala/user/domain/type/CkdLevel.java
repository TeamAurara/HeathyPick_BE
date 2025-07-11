package com.soongsil.eolala.user.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.soongsil.eolala.global.support.error.GlobalErrorType;
import com.soongsil.eolala.global.support.error.GlobalException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CkdLevel {
    LEVEL_1("1"),
    LEVEL_2("2"),
    LEVEL_3A("3a"),
    LEVEL_3B("3b"),
    LEVEL_4("4"),
    LEVEL_5("5");

    private final String description;

    @JsonCreator
    public static CkdLevel from(String level) {
        for (CkdLevel each : CkdLevel.values()) {
            if (each.getDescription().equalsIgnoreCase(level.trim())) {
                return each;
            }
        }
        throw new GlobalException(GlobalErrorType.INVALID_REQUEST_ARGUMENT);
    }
}
