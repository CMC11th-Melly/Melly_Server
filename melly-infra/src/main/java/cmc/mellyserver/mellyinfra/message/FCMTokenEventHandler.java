package cmc.mellyserver.mellyinfra.message;

import cmc.mellyserver.mellycore.comment.application.event.CreateFCMTokenEvent;
import cmc.mellyserver.mellycore.comment.application.event.RemoveFCMTokenEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class FCMTokenEventHandler {

    private final FCMService fcmService;

    @TransactionalEventListener
    public void createFCMToken(CreateFCMTokenEvent event) {

        fcmService.saveToken(event.getUserId(), event.getFcmToken());
    }

    @TransactionalEventListener
    public void removeFCMToken(RemoveFCMTokenEvent event) {

        fcmService.deleteToken(event.getUserId());
    }
}
