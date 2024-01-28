package cmc.mellyserver.auth.certificate;

import static cmc.mellyserver.auth.certificate.CertificateConstants.*;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Repository;

import cmc.mellyserver.dbredis.repository.RedisRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class CertificationNumberDao {

    private final RedisRepository repository;

    public void save(String email, String certificationNumber) {
        repository.save(PREFIX_CERTIFICATION + email, certificationNumber, LIMIT_TIME_CERTIFICATION_NUMBER,
            TimeUnit.MILLISECONDS);
    }

    public String get(String email) {
        return repository.get(PREFIX_CERTIFICATION + email);
    }

    public void delete(String email) {
        repository.delete(PREFIX_CERTIFICATION + email);
    }

    public Boolean hasKey(String email) {
        return repository.hasKey(PREFIX_CERTIFICATION + email);
    }

}
