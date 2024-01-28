package cmc.mellyserver.auth.certificate;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import cmc.mellyserver.auth.service.dto.request.EmailCertificationRequest;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Profile({"local", "prod"})
@Repository
@RequiredArgsConstructor
class EmailCertificationService implements CertificationService {

    private final CertificationNumberDao certificationNumberDao;

    private final ApplicationEventPublisher applicationEventPublisher;

    public void sendCertification(String email) {

        String certificationNumber = RandomNumberGenerator.makeRandomNumber();
        certificationNumberDao.save(email, certificationNumber);
        applicationEventPublisher.publishEvent(new CertificationCompletedEvent(email, certificationNumber));
    }

    public void verify(EmailCertificationRequest requestDto) {

        if (isVerify(requestDto)) {
            throw new BusinessException(ErrorCode.NOT_VALID_ERROR);
        }

        certificationNumberDao.delete(requestDto.email());
    }

    private boolean isVerify(EmailCertificationRequest requestDto) {
        return !(certificationNumberDao.hasKey(requestDto.email())
            && certificationNumberDao.get(requestDto.email())
            .equals(requestDto.certificationNumber()));
    }
}