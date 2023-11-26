package cmc.mellyserver.auth.token;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import cmc.mellyserver.auth.service.dto.response.RefreshTokenDto;
import cmc.mellyserver.dbcore.user.enums.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider implements TokenProvider {

    private final Key secretKey;

    private final long accessExpirationTime;

    private final long refreshExpirationTime;

    public JwtTokenProvider(
        @Value("${app.auth.token-secret}") String secret,
        @Value("${app.auth.access-expiration-time}") long accessExpirationTime,
        @Value("${app.auth.refresh-expiration-time}") long refreshExpirationTime
    ) {
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String createAccessToken(Long userId, RoleType roleType) {

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
            .signWith(secretKey)
            .setIssuedAt(now)
            .setExpiration(expireDate)
            .setSubject(TokenConstants.ACCESS_TOKEN)
            .claim(TokenConstants.AUTHORITY_KEY, String.valueOf(userId))
            .claim(TokenConstants.ROLE, roleType.getDescription())
            .compact();
    }

    @Override
    public RefreshTokenDto createRefreshToken(Long userId, RoleType roleType) {

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpirationTime);

        String token = Jwts.builder()
            .signWith(secretKey)
            .setIssuedAt(now)
            .setExpiration(expireDate)
            .setSubject(TokenConstants.REFRESH_TOKEN)
            .claim(TokenConstants.AUTHORITY_KEY, String.valueOf(userId))
            .claim(TokenConstants.ROLE, roleType.getDescription())
            .compact();

        return new RefreshTokenDto(token, refreshExpirationTime);
    }

    @Override
    public Authentication getAuthentication(String token) {

        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(
                claims.get(TokenConstants.ROLE).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        User principal = new User(claims.get(TokenConstants.AUTHORITY_KEY).toString(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    @Override
    public long getLastExpiredTime(String token) {

        Date expirationDate = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration();

        return expirationDate.getTime() - new Date().getTime();

    }

    @Override
    public long extractUserId(String accessToken) {

        try {
            String memberId = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .get(TokenConstants.AUTHORITY_KEY)
                .toString();

            return Long.parseLong(memberId);
        } catch (final JwtException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
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