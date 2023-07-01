package cmc.mellyserver.mellyappexternalapi.healthcheck;

import cmc.mellyserver.mellydomain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthCheckService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public String check() throws InterruptedException {
        log.info("쿼리 실행");
        userRepository.findAll();
        return "테스트";
    }
}
