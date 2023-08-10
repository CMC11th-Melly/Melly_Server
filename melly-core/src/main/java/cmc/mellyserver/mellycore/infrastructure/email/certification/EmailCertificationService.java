package cmc.mellyserver.mellycore.infrastructure.email.certification;

public interface EmailCertificationService {

    void sendEmailForCertification(String email);

    void verifyEmail(EmailCertificationRequest requestDto);
}
