package cmc.mellyserver.common.mock;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import cmc.mellyserver.auth.certificate.CertificationService;
import cmc.mellyserver.auth.dto.request.EmailCertificationRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("test")
public class MockMailCertificationService implements CertificationService {

    @Override
    public void sendCertification(String email) {

    }

    @Override
    public void verify(EmailCertificationRequest requestDto) {

    }

}
