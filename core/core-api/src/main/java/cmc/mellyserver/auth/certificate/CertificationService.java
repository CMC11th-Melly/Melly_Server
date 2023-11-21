package cmc.mellyserver.auth.certificate;

import cmc.mellyserver.auth.dto.request.EmailCertificationRequest;

public interface CertificationService {

    void sendCertification(String email);

    void verify(EmailCertificationRequest requestDto);

}
