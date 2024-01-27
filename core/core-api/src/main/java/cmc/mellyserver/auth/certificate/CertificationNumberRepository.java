package cmc.mellyserver.auth.certificate;

import static cmc.mellyserver.auth.certificate.CertificateConstants.*;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class CertificationNumberRepository {

    private final RedisTemplate<String, String> redisTemplate;

    // 인증번호 유효기간 : 3분
    public void save(String email, String certificationNumber) {

        redisTemplate.opsForValue()
            .set(PREFIX_CERTIFICATION + email, certificationNumber,
                Duration.ofSeconds(LIMIT_TIME_CERTIFICATION_NUMBER));

    }

    public String get(String email) {
        return redisTemplate.opsForValue().get(PREFIX_CERTIFICATION + email);
    }

    public void remove(String email) {
        redisTemplate.delete(PREFIX_CERTIFICATION + email);
    }

    public Boolean hasKey(String email) {
        return redisTemplate.hasKey(PREFIX_CERTIFICATION + email);
    }

}
