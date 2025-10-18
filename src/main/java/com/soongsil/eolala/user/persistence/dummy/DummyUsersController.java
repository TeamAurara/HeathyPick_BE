package com.soongsil.eolala.user.persistence.dummy;

import com.soongsil.eolala.global.support.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class DummyUsersController {

    private final DummyUsers dummyUsers;
    private static final int DUMMY_USER_COUNT = 15;

    @Operation(summary = "더미 유저 생성 API", description = "더미 유저 생성")
    @PostMapping("/dummy")
    public ApiResponse<?> create() {
        dummyUsers.createDummyUsers(DUMMY_USER_COUNT);
        return ApiResponse.success();
    }
}
