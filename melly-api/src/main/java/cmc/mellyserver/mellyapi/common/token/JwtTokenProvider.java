package cmc.mellyserver.mellyapi.common.token;

import cmc.mellyserver.mellycommon.enums.RoleType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parserBuilder;

@Slf4j
@Component
public class JwtTokenProvider {

	protected static final String AUTHORITIES_KEY = "auth";
	protected final String secret;
	protected final long tokenValidityInMilliseconds;

	protected Key key;

	public JwtTokenProvider(@Value("${app.auth.tokenSecret}") String secret, @Value("${app.auth.tokenExpiry}") long tokenValidityInSeconds) {

		this.secret = secret;
		this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	// 토큰 생성
	public String createToken(Long userSeq, RoleType role) {

		// 현재시간
		long now = (new Date()).getTime();

		// 만료되는 날짜
		Date validity = new Date(now + this.tokenValidityInMilliseconds);

		return builder()
				.setSubject(Long.toString(userSeq))
				.claim(AUTHORITIES_KEY, role.toString())
				.signWith(key, SignatureAlgorithm.HS512)
				.setExpiration(validity)
				.compact();

	}

	public Authentication getAuthentication(String token) {

		Claims claims = parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();

		Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public boolean validateToken(String token) {
		try {
			parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
		}
		return false;
	}

}