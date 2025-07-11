package com.soongsil.eolala.auth.dto;

import com.soongsil.eolala.user.domain.type.Gender;

/**
 * 내부 User 테이블 필드와 1:1 매핑되는 유저 정보 DTO
 */
public record UserInfoDto(
	Long     userId,
	String   email,
	String   nickname,
	String   profileImageUrl,
	Gender   gender,
	int      age,
	boolean  isOnboarded
) {}