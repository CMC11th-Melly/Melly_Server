package cmc.mellyserver.health;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthCheckController {

    @GetMapping("/health")
    public String healthCheck()
    {
        return "떡잎마을방범대 파이어~";
    }

    @GetMapping("/authTest")
    public String authCheck()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName() + " 정상적으로 JWT 인증됐습니다.";

    }
}
