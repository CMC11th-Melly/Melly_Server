package cmc.mellyserver.auth.certification;

import static cmc.mellyserver.mail.EmailConstants.LIMIT_TIME_CERTIFICATION_NUMBER;
import static cmc.mellyserver.mail.EmailConstants.PREFIX_CERTIFICATION;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
class EmailCertificationNumberDao implements CertificationNumberDao {

    private final StringRedisTemplate redisTemplate;

    /**
     * 이메일 인증 번호를 Redis에 저장합니다.
     * <p>
     * 인증번호 유효기간은 3분으로 설정했습니다.
     */
    @Override
    public void saveCertificationNumber(String email, String certificationNumber) {

        redisTemplate.opsForValue()
                .set(  PREFIX_CERTIFICATION + email, certificationNumber, Duration.ofSeconds(LIMIT_TIME_CERTIFICATION_NUMBER));

    }

    @Override
    public String getCertificationNumber(String email) {
        return redisTemplate.opsForValue().get(PREFIX_CERTIFICATION + email);
    }

    @Override
    public void removeCertificationNumber(String email) {
        redisTemplate.delete(PREFIX_CERTIFICATION + email);
    }

    @Override
    public boolean hasKey(String email) {
        return redisTemplate.hasKey(PREFIX_CERTIFICATION + email);
    }
}
