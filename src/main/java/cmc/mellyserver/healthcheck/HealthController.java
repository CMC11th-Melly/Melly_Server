package cmc.mellyserver.healthcheck;

import cmc.mellyserver.common.event.CustomEventPublisher;
import cmc.mellyserver.notification.application.FCMService;
import cmc.mellyserver.notification.domain.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class HealthController {

    private final CustomEventPublisher customEventPublisher;

    @GetMapping("/")
    public String test()
    {
        customEventPublisher.publish("efghwhrwhrhrhwhw", NotificationType.COMMENT,"내 메모리에 새 댓글이 달렸어요! 확인해보세요");
        return "check";
    }
}
