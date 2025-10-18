package com.soongsil.eolala.user.persistence.dummy;

import com.soongsil.eolala.global.support.error.GlobalErrorType;
import com.soongsil.eolala.global.support.error.GlobalException;
import com.soongsil.eolala.user.domain.User;
import com.soongsil.eolala.user.domain.type.Gender;
import com.soongsil.eolala.user.domain.type.Role;
import com.soongsil.eolala.user.domain.type.SocialType;
import com.soongsil.eolala.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class DummyUsers {
    private final UserRepository userRepository;

    private final List<String> dummyNames = loadNames();
    private final List<String> dummyEmails = loadEmail();
    private static final List<Gender> GENDERS = List.of(Gender.MALE, Gender.FEMALE);
    private static final List<String> DOMAINS = List.of("@naver.com", "@gmail.com");
    private static final String PROFILE_BASE_URL = "http://img.kakaocdn.net/profiles/";
    private static final String EXTENSIONS_PNG = ".png";

    @Transactional
    public void createDummyUsers(int count) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String name = dummyNames.get(i % dummyNames.size());
            Gender gender = GENDERS.get(i % 2);
            int age = ThreadLocalRandom.current().nextInt(15, 100);
            String providerId = String.valueOf(ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L));

            String email = generateEmailFromName(i);
            String profile = generateProfileImageUrl(name, i);

            boolean onboarded = i % 4 != 0;
            users.add(User.builder()
                    .email(email)
                    .providerId(providerId)
                    .socialType(SocialType.KAKAO)
                    .nickname(name)
                    .gender(gender)
                    .age(age)
                    .profileImageUrl(profile)
                    .isOnboarded(onboarded)
                    .role(Role.USER)
                    .build()
            );
        }

        userRepository.saveAll(users);
    }

    private String generateEmailFromName(int index) {
        String email = dummyEmails.get(index);
        String domain = DOMAINS.get(ThreadLocalRandom.current().nextInt(DOMAINS.size()));
        return email + domain;
    }

    private static List<String> loadEmail() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(DummyUsers.class.getResourceAsStream("/data/email.txt")))
        )) {
            return reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .toList();
        } catch (IOException | NullPointerException e) {
            throw new GlobalException(GlobalErrorType.FAILED_LOAD_RESOURCE);
        }
    }

    private static List<String> loadNames() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(DummyUsers.class.getResourceAsStream("/data/names.txt")))
        )) {
            return reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .toList();
        } catch (IOException | NullPointerException e) {
            throw new GlobalException(GlobalErrorType.FAILED_LOAD_RESOURCE);
        }
    }

    private static String generateProfileImageUrl(String name, int index) {
        String dummyName = name.replaceAll("[^a-zA-Z0-9가-힣]", "");
        return PROFILE_BASE_URL + dummyName + index + EXTENSIONS_PNG;
    }
}
