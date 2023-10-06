package cmc.mellyserver.notification.email.certification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

import static cmc.mellyserver.notification.email.constant.EmailConstants.LIMIT_TIME_CERTIFICATION_NUMBER;
import static cmc.mellyserver.notification.email.constant.EmailConstants.PREFIX_CERTIFICATION;

@RequiredArgsConstructor
@Repository
class EmailCertificationNumberDao implements EmailCertificationDao {

    private final StringRedisTemplate redisTemplate;

    /**
     * 이메일 인증 번호를 Redis에 저장합니다.
     * <p>
     * 인증번호 유효기간은 3분으로 설정했습니다.
     */
    @Override
    public void saveEmailCertificationNumber(String email, String certificationNumber) {

        redisTemplate.opsForValue()
                .set(PREFIX_CERTIFICATION + email, certificationNumber, Duration.ofSeconds(LIMIT_TIME_CERTIFICATION_NUMBER));

    }

    @Override
    public String getEmailCertificationNumber(String email) {
        return redisTemplate.opsForValue().get(PREFIX_CERTIFICATION + email);
    }

    @Override
    public void removeEmailCertificationNumber(String email) {
        redisTemplate.delete(PREFIX_CERTIFICATION + email);
    }

    @Override
    public boolean hasKey(String email) {
        return redisTemplate.hasKey(PREFIX_CERTIFICATION + email);
    }
}
