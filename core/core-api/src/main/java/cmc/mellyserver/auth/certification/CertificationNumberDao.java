package cmc.mellyserver.auth.certification;

public interface CertificationNumberDao {

    void saveCertificationNumber(String email, String certificationNumber);

    String getCertificationNumber(String email);

    void removeCertificationNumber(String email);

    boolean hasKey(String email);
}
