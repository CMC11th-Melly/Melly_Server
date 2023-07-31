package cmc.mellyserver.mellycore.common.message;

public interface EmailCertification {

    void createEmail(String email, String certificationNumber);

    String getEmailCertification(String email);

    void removeEmailCertification(String email);

    boolean hasKey(String email);
}
