package cmc.mellyserver.mellyapi.common.healthcheck;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HealthCheckController {

    @Transactional(readOnly = true)
    @GetMapping("/health")
    public String healthCheck() {
        return "hello";
    }
}
