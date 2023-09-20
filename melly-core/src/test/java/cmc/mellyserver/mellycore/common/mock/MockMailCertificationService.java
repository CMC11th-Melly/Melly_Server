package cmc.mellyserver.mellycore.config.mockapi;

import cmc.mellyserver.mellycore.infrastructure.email.certification.EmailCertificationRequest;
import cmc.mellyserver.mellycore.infrastructure.email.certification.EmailCertificationService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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
