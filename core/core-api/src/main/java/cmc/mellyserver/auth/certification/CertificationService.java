package cmc.mellyserver.auth.certification;

public interface CertificationService {

	void sendCertification(String email);

	void verify(EmailCertificationRequest requestDto);

}
