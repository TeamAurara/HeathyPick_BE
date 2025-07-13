package com.soongsil.eolala.auth.dto;

import com.soongsil.eolala.user.domain.User;

public record UserInfoDto(
        Long userId,
        String email,
        String nickname,
        String profileImageUrl,
        String gender,
        Integer age,
        boolean isOnboarded
) {
    public static UserInfoDto from(User user) {
        return new UserInfoDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImageUrl(),
                user.getGender().toString(),
                user.getAge(),
                user.isOnboarded()
        );
    }
}