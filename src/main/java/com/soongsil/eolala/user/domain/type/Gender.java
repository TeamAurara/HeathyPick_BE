package com.soongsil.eolala.user.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.soongsil.eolala.global.support.error.GlobalErrorType;
import com.soongsil.eolala.global.support.error.GlobalException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {

    MALE("남자"),
    FEMALE("여자"),
    ;

    private final String description;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Gender from(String gender) {
        for (Gender each : Gender.values()) {
            if (each.getDescription().equalsIgnoreCase(gender.trim())) {
                return each;
            }
        }
        throw new GlobalException(GlobalErrorType.INVALID_REQUEST_ARGUMENT);
    }
}
