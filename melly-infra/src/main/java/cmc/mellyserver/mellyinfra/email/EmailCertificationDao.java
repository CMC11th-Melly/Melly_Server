package cmc.mellyserver.mellyinfra.email;

public interface EmailCertificationDao {

    void createEmail(String email, String certificationNumber);

    String getEmailCertification(String email);

    void removeEmailCertification(String email);

    boolean hasKey(String email);
}
