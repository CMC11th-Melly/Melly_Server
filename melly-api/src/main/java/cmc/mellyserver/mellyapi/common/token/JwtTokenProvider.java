package cmc.mellyserver.mellyapi.common.token;

import cmc.mellyserver.mellyapi.auth.application.dto.response.RefreshTokenDto;
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

@Slf4j
@Component
public class JwtTokenProvider {

    protected static final String AUTHORITIES_KEY = "access";

    protected final String secret;

    private final long accessExpirationTime;

    private final long refreshExpirationTime;

    protected Key secretKey;

    public JwtTokenProvider(@Value("${app.auth.token-secret}") String secret,
                            @Value("${app.auth.access-expiration-time}") long accessExpirationTime,
                            @Value("${app.auth.refresh-expiration-time}") long refreshExpirationTime
    ) {
        this.secret = secret;
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public String createAccessToken(Long userId, RoleType roleType) {

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
                .signWith(secretKey)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .setSubject(String.valueOf(userId))
                .claim(AUTHORITIES_KEY, roleType.getCode())
                .compact();
    }

    public RefreshTokenDto createRefreshToken(Long userId, RoleType roleType) {

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpirationTime);

        String token = Jwts.builder()
                .signWith(secretKey)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .setSubject(String.valueOf(userId))
                .claim(AUTHORITIES_KEY, roleType.getCode())
                .compact();

        return new RefreshTokenDto(token, refreshExpirationTime);
    }


    public Authentication getAuthentication(String token) {

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
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

    public Long getLastExpireTime(String token) {

        Date expirationDate = Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody().getExpiration();

        return expirationDate.getTime() - new Date().getTime();

    }

    public Long extractMemberId(final String accessToken) {
        try {
            String memberId = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .getSubject();
            return Long.parseLong(memberId);
        } catch (final JwtException e) {
            throw new IllegalArgumentException();
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("엑세스 토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}