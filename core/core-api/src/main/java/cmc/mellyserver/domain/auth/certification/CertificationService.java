package cmc.mellyserver.domain.auth.certification;

public interface CertificationService {

    void sendCertification(String email);

    void verify(EmailCertificationRequest requestDto);
}
