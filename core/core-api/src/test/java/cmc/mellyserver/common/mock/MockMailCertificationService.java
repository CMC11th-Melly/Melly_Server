package cmc.mellyserver.common.mock;

import cmc.mellyserver.mellycore.infrastructure.email.certification.EmailCertificationRequest;
import cmc.mellyserver.mellycore.infrastructure.email.certification.EmailCertificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("test")
public class MockMailCertificationService implements EmailCertificationService {
    @Override
    public void sendEmailForCertification(String email) {

    }

    @Override
    public void verifyEmail(EmailCertificationRequest requestDto) {

    }
}
