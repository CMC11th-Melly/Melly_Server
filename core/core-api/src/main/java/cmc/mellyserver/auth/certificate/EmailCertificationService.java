package cmc.mellyserver.auth.certificate;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import cmc.mellyserver.auth.service.dto.request.EmailCertificationRequest;
import lombok.RequiredArgsConstructor;

@Profile({"local", "prod"})
@Repository
@RequiredArgsConstructor
class EmailCertificationService implements CertificationService {

    private final CertificationNumberRepository certificationNumberRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 인증 이메일 전송 기능
     */
    public void sendCertification(String email) {

        String certificationNumber = RandomNumberGenerator.makeRandomNumber();
        certificationNumberRepository.save(email, certificationNumber);
        applicationEventPublisher.publishEvent(new CertificationCompletedEvent(email, certificationNumber));

    }

    // 인증번호 일치 여부 확인
    public void verify(EmailCertificationRequest requestDto) {

        if (isVerify(requestDto)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        // 인증 후 DB에서 인증번호 삭제
        certificationNumberRepository.remove(requestDto.email());
    }

    // 인증번호 일치 여부 확인 내부 로직
    private boolean isVerify(EmailCertificationRequest requestDto) {
        return !(certificationNumberRepository.hasKey(requestDto.email())
            && certificationNumberRepository.get(requestDto.email())
            .equals(requestDto.certificationNumber()));
    }

}