package com.soongsil.eolala.auth.domain;

import com.soongsil.eolala.global.domain.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefreshToken extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "refresh_token", nullable = false, length = 512)
	private String refreshToken;

	/** 토큰 발급 시각 */
	@Column(name = "issued_at", nullable = false)
	private Instant issuedAt;

	/** 토큰 만료 시각 */
	@Column(name = "expires_at", nullable = false)
	private Instant expiresAt;

	/** 토큰 정보 업데이트 (토큰 재발급 시 사용) */
	public void updateToken(String newRefreshToken, Instant newIssuedAt, Instant newExpiresAt) {
		this.refreshToken = newRefreshToken;
		this.issuedAt = newIssuedAt;
		this.expiresAt = newExpiresAt;
	}

	/** 토큰 만료 여부 확인 */
	public boolean isExpired() {
		return expiresAt.isBefore(Instant.now());
	}
}