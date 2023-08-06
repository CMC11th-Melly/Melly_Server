package cmc.mellyserver.mellycore.common.port.message;

public interface EmailCertificationService {

    void createEmail(String email, String certificationNumber);

    String getEmailCertification(String email);

    void removeEmailCertification(String email);

    boolean hasKey(String email);
}
