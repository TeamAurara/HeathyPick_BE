package com.soongsil.eolala.user.domain;

import com.soongsil.eolala.user.domain.type.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private UserOnboarding onboarding;

    @Builder
    public User(String email, String nickname, Gender gender, int age, String profileImageUrl, boolean isOnboarded) {
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.profileImageUrl = profileImageUrl;
        this.isOnboarded = isOnboarded;
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
}
