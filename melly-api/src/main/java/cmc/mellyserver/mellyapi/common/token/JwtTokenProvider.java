package cmc.mellyserver.mellyapi.common.token;

import cmc.mellyserver.mellyapi.auth.application.dto.response.RefreshTokenDto;
import cmc.mellyserver.mellycore.user.domain.enums.RoleType;
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
public class JwtTokenProvider implements TokenProvider {

    protected static final String ROLE = "role";

    protected static final String AUTHORITY_KEY = "auth";

    protected static final String ACCESS_TOKEN = "access_token";

    protected static final String REFRESH_TOKEN = "refresh_token";


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


    @Override

    public String createAccessToken(Long userId, RoleType roleType) {

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
                .signWith(secretKey)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .setSubject(ACCESS_TOKEN)
                .claim(AUTHORITY_KEY, String.valueOf(userId))
                .claim(ROLE, roleType.getCode())
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
                .setSubject(REFRESH_TOKEN)
                .claim(AUTHORITY_KEY, String.valueOf(userId))
                .claim(ROLE, roleType.getCode())
                .compact();


        return new RefreshTokenDto(token, refreshExpirationTime);
    }


    @Override
    public Authentication getAuthentication(String token) {

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(ROLE).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.get(AUTHORITY_KEY).toString(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }


    @Override
    public Long getLastExpireTime(String token) {

        Date expirationDate = Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody().getExpiration();

        return expirationDate.getTime() - new Date().getTime();

    }


    @Override
    public Long extractUserId(final String accessToken) {

        try {
            String memberId = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody().get(AUTHORITY_KEY).toString();

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