package cmc.mellyserver.healthcheck;

import cmc.mellyserver.common.event.CustomEventPublisher;
import cmc.mellyserver.memory.domain.MemoryQueryRepository;
import cmc.mellyserver.notification.application.FCMService;
import cmc.mellyserver.notification.domain.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HealthController {


    @GetMapping("/")
    public String test()
    {
        return "check";
    }
}
