package com.soongsil.eolala.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {

	private final SecretKey key;
	private final long accessTokenValidity;
	private final long refreshTokenValidity;

	public JwtProvider(@Value("${jwt.secret}") String secretKeyBase64,
					   @Value("${jwt.access-token-validity-seconds}") long accessTokenValidity,
					   @Value("${jwt.refresh-token-validity-seconds}") long refreshTokenValidity) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.accessTokenValidity = accessTokenValidity;
		this.refreshTokenValidity = refreshTokenValidity;
	}

	public String generateAccessToken(Long userId, List<String> roles) {
		Date now    = new Date();
		Date expiry = new Date(now.getTime() + accessTokenValidity);

		return Jwts.builder()
			.subject(String.valueOf(userId))
			.claim("roles", roles)
			.claim("type", "access")
			.issuedAt(now)
			.expiration(expiry)
			.signWith(key, Jwts.SIG.HS256)
			.compact();
	}

	public String generateRefreshToken(Long userId, List<String> roles) {
		Date now    = new Date();
		Date expiry = new Date(now.getTime() + refreshTokenValidity);

		return Jwts.builder()
			.subject(String.valueOf(userId))
			.claim("roles", roles)
			.claim("type", "refresh")
			.issuedAt(now)
			.expiration(expiry)
			.signWith(key, Jwts.SIG.HS256)
			.compact();
	}

	private Claims parseClaims(String token) {
		Jws<Claims> jws = Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token);
		return jws.getPayload();
	}

	public Authentication getAuthentication(String token) {
		Claims claims = parseClaims(token);

		@SuppressWarnings("unchecked")
		List<String> roles = (List<String>) claims.get("roles");

		var authorities = roles.stream()
			.map(SimpleGrantedAuthority::new)
			.toList();

		return new UsernamePasswordAuthenticationToken(
			claims.getSubject(), null, authorities
		);
	}

	public boolean validateToken(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	public long getAccessTokenValidityMs() {
		return accessTokenValidity;
	}

	public long getRefreshTokenValidityMs() {
		return refreshTokenValidity;
	}
}