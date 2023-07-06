package cmc.mellyserver.mellyapi.common.healthcheck;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthCheckController {

    @Transactional(readOnly = true)
    @GetMapping("/health")
    public String healthCheck() throws InterruptedException {
        return "test";
    }
}
