package cmc.mellyserver.healthcheck;

import cmc.mellyserver.healthcheck.dto.MultipartTestRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthCheckController {

    @Operation(summary = "헬스 체크용 API")
    @GetMapping("/health")
    public String healthCheck()
    {
        return "떡잎마을방범대 파이어~";
    }

    @Operation(summary = "액세스 토큰 Authorization Header에 추가 시 인증 통과 테스트")
    @GetMapping("/authTest")
    public String authCheck()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName() + " 정상적으로 JWT 인증됐습니다.";

    }

    @Operation(summary = "멀티파트 통신 테스트")
    @PostMapping("/imageTest")
    public String multipartTest(MultipartTestRequest multipartTestRequest)
    {
        String originalFilename = multipartTestRequest.getImage().getOriginalFilename();
        return "멀티 파트 파일 전송! " + originalFilename;
    }
}
