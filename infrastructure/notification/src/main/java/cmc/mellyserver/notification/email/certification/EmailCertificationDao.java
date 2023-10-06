package cmc.mellyserver.notification.email.certification;

public interface EmailCertificationDao {

    void saveEmailCertificationNumber(String email, String certificationNumber);

    String getEmailCertificationNumber(String email);

    void removeEmailCertificationNumber(String email);

    boolean hasKey(String email);
}
