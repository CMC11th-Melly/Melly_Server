package cmc.mellyserver.common.event;

import cmc.mellyserver.notification.domain.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(String fcmToken, NotificationType notificationType,String message,String uid, Long memoryId)
    {
        if(!fcmToken.isBlank())
        {
            log.info("Publishing custom event");
            CustomEvent customEvent = new CustomEvent(this, fcmToken,notificationType, message,uid,memoryId);
            applicationEventPublisher.publishEvent(customEvent);
        }

    }
}
