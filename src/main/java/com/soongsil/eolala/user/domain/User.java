package com.soongsil.eolala.user.domain;

import com.soongsil.eolala.global.domain.BaseEntity;
import com.soongsil.eolala.user.domain.type.Gender;
import com.soongsil.eolala.user.domain.type.Role;
import com.soongsil.eolala.user.domain.type.SocialType;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = true, unique = true)
    private String email;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

	@Enumerated(EnumType.STRING)
	@Column(name = "social_type", nullable = false)
	private SocialType socialType;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "profile_image_url", nullable = true)
    private String profileImageUrl;

    @Column(name = "is_onboarded", nullable = false)
    private boolean isOnboarded;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private UserOnboarding onboarding;

    @Builder
    public User(String email, String providerId, SocialType socialType, String nickname, Gender gender, int age, String profileImageUrl, boolean isOnboarded, Role role) {
        this.email = email;
        this.providerId = providerId;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.profileImageUrl = profileImageUrl;
        this.isOnboarded = isOnboarded;
		this.socialType = socialType;
		if (role == null) {
			this.role = Role.USER;
		} else {
			this.role = role;
		}
    }

    public void updateOnboarding(UserOnboarding onboarding) {
        this.onboarding = onboarding;
        this.isOnboarded = true;
    }

    public void updateUser(String nickname, Gender gender, int age) {
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
    }

    public void updateRole(Role role) {
        this.role = role;
    }
}
