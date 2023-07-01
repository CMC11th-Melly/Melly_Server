package cmc.mellyserver.mellyappexternalapi.healthcheck;

import cmc.mellyserver.mellyappexternalapi.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    @GetMapping("/health")
    public String healthCheck() throws InterruptedException {
        return healthCheckService.check();
    }

    @GetMapping("/authTest")
    public ResponseEntity<CommonResponse> authCheck() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(new CommonResponse(200, "정상적으로 인증되었습니다"));
    }
}
