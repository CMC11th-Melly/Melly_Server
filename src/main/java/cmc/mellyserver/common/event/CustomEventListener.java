package cmc.mellyserver.common.event;

import cmc.mellyserver.notification.application.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomEventListener {

    private final FCMService fcmService;

    @Async
    @EventListener
    public void handleEvent(CustomEvent event) throws IOException {
         fcmService.sendMessageTo(event.getFcmToken(),event.getNotificationType(),event.getMessage(),event.getUid(),event.getMemoryId());
    }
}
