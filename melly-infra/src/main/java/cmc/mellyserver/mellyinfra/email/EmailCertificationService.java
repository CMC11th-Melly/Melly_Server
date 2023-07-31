package cmc.mellyserver.mellyinfra.email;

public interface EmailCertificationService {
    void sendEmailForCertification(String email);

    void verifyEmail(EmailCertificationRequest requestDto);
}
