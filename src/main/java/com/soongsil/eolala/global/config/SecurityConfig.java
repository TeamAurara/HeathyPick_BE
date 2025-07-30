package com.soongsil.eolala.global.config;

import com.soongsil.eolala.auth.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Security 설정 클래스입니다.
 * - JWT 토큰 기반 인증을 지원합니다.
 * - 경로별 세부 권한 설정을 @PreAuthorize 메타 어노테이션으로 위임합니다.
 * - 기본적인 보안 설정만 중앙에서 관리합니다.
 * - 도메인별 보안 정책은 각 컨트롤러에서 명시적으로 선언합니다.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize 활성화
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

	private final JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)

			.cors(Customizer.withDefaults())

			.authorizeHttpRequests(auth -> auth
				// 정적 리소스는 항상 허용
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				// 개발/운영 도구 경로 허용
				.requestMatchers(
					"/swagger-ui/**",
					"/v3/api-docs/**",
					"/swagger-ui.html",
					"/swagger-resources/**",
					"/webjars/**",
					"/favicon.ico",
					"/actuator/health"
				).permitAll()
				// 루트 경로 허용
				.requestMatchers("/").permitAll()
				// 나머지 모든 요청은 메타 어노테이션(@PreAuthorize)으로 권한 검사
				.anyRequest().permitAll()
			)

			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
	

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/static/**", "/css/**", "/js/**", "/images/**")
			.addResourceLocations("classpath:/static/", "classpath:/public/")
			.setCachePeriod(3600);

		registry.addResourceHandler("/favicon.ico")
			.addResourceLocations("classpath:/static/")
			.setCachePeriod(86400);

		registry.addResourceHandler("/robots.txt")
			.addResourceLocations("classpath:/static/")
			.setCachePeriod(86400);
	}
}
