package cmc.mellyserver.auth.certification;

import cmc.mellyserver.common.event.SignupCertificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Profile({"local", "prod"})
@Repository
class EmailCertificationService implements CertificationService {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final CertificationNumberDao certificationNumberDao;

    /**
     * 인증 이메일 전송 기능
     */
    public void sendCertification(String email) {

        String certificationNumber = RandomNumberGenerator.makeRandomNumber();

        applicationEventPublisher.publishEvent(new SignupCertificationEvent(email, certificationNumber));
        certificationNumberDao.saveCertificationNumber(email, certificationNumber);
    }


    // 인증번호 일치 여부 확인
    public void verify(EmailCertificationRequest requestDto) {

        if (isVerify(requestDto)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        // 인증 후 DB에서 인증번호 삭제
        certificationNumberDao.removeCertificationNumber(requestDto.getEmail());
    }


    // 인증번호 일치 여부 확인 내부 로직
    private boolean isVerify(EmailCertificationRequest requestDto) {
        return !(certificationNumberDao.hasKey(requestDto.getEmail()) && certificationNumberDao.getCertificationNumber(requestDto.getEmail()).equals(requestDto.getCertificationNumber()));
    }
}