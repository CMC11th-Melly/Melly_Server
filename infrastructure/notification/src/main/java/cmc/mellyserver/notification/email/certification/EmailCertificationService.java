package cmc.mellyserver.notification.email.certification;

public interface EmailCertificationService {

    void sendEmailForCertification(String email);

    void verifyEmail(EmailCertificationRequest requestDto);
}
