package com.soongsil.eolala.home.presentation;

import com.soongsil.eolala.global.securitiy.annotation.AuthenticatedApi;
import com.soongsil.eolala.global.support.dto.response.ApiResponse;
import com.soongsil.eolala.home.application.HomeService;
import com.soongsil.eolala.home.dto.response.HomeResponse;
import com.soongsil.eolala.home.presentation.docs.HomeControllerDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController implements HomeControllerDocs {

    private final HomeService homeService;

    @AuthenticatedApi(reason = "사용자의 홈 화면 정보 조회를 위해 인증 필요")
    @GetMapping("/{userId}")
    @Override
    public ApiResponse<HomeResponse> getHomeSummary(@PathVariable Long userId) {
        HomeResponse response = homeService.getHomeSummary(userId);
        return ApiResponse.success(response);
    }
}