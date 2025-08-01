package com.soongsil.eolala.global.config;

import com.soongsil.eolala.auth.client.kakao.KakaoProperties;
import com.soongsil.eolala.global.client.openai.OpenAIProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {OpenAIProperties.class, KakaoProperties.class})
public class ConfigurationPropsConfig {
}
