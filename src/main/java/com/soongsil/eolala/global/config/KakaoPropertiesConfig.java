package com.soongsil.eolala.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import com.soongsil.eolala.auth.client.kakao.KakaoProperties;

@Configuration
@EnableConfigurationProperties(KakaoProperties.class)
public class KakaoPropertiesConfig {
}