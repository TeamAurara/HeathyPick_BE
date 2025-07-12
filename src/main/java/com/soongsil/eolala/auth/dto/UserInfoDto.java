package com.soongsil.eolala.auth.dto;

import com.soongsil.eolala.user.domain.type.Gender;

public record UserInfoDto(
	Long     userId,
	String   email,
	String   nickname,
	String   profileImageUrl,
	Gender   gender,
	int      age,
	boolean  isOnboarded
) {}