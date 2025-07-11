package com.soongsil.eolala.user.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Activity {
    MOVE_HARD(3, "격한 운동을 많이 해요"),
    MOVE_LESS(1, "약한 운동을 많이 해요"),
    MOVE_WELL(2, "잘 움직여요"),
    MOVE_NONE(0, "몸을 거의 움직이지 않아요"),
    ;

    private final int score;
    private final String description;
}
