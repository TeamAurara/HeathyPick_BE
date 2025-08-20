package com.soongsil.eolala.global.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

	@CreatedDate
	private LocalDateTime createdDate;

	@LastModifiedDate
	private LocalDateTime modifiedDate;

	// 생성자 정보 (JWT 인증으로 현재 사용자 자동 설정)
	//@CreatedBy
	private String createdBy;

	// 수정자 정보 (JWT 인증으로 현재 사용자 자동 설정)
	//@LastModifiedBy
	private String lastModifiedBy;

	private boolean deleted = false;
}
