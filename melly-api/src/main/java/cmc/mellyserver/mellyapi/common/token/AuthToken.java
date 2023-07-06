package cmc.mellyserver.mellyapi.common.token;

import java.security.Key;
import java.util.Date;

import cmc.mellyserver.mellycommon.enums.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {

	private static final String AUTHORITIES_KEY = "role";
	@Getter
	private final String token;
	private final Key key;

	AuthToken(Long userId, RoleType roleType, Date expiry, Key key) {
		String role = roleType.toString();
		this.key = key;
		this.token = createAuthToken(userId, role, expiry);
	}

	private String createAuthToken(Long userId, String role, Date expiry) {
		return Jwts.builder()
			.setSubject(userId.toString())
			.claim(AUTHORITIES_KEY, role)
			.signWith(key, SignatureAlgorithm.HS256)
			.setExpiration(expiry)
			.compact();
	}

	public boolean validate() {
		return this.getTokenClaims() != null;
	}

	public Claims getTokenClaims() {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (SecurityException e) {
			log.info("Invalid JWT signature.");
			throw new JwtException("잘못된 JWT 시그니처입니다.");
		} catch (MalformedJwtException e) {
			log.info("Invalid JWT token.");
			throw new JwtException("유효하지 않은 JWT 토큰입니다.");
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
			throw new JwtException("JWT 토큰 기한이 만료됐습니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT token compact of handler are invalid.");
			throw new JwtException("JWT token compact of handler are invalid.");
		}
		return null;
	}
}

