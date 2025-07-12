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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)

			.cors(Customizer.withDefaults())

			.authorizeHttpRequests(auth -> auth

				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.requestMatchers(
					"/swagger-ui/**", "/v3/api-docs/**",
					"/swagger-ui.html", "/favicon.ico"
				).permitAll()

				.requestMatchers("/api/auth/**").permitAll()

				.anyRequest().authenticated()
			)

			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
