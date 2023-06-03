package cmc.mellyserver.mellyapi.common.event;

import java.io.IOException;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import cmc.mellyserver.mellyapi.notification.application.FCMService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomEventListener {

	private final FCMService fcmService;

	@Async
	@EventListener
	public void handleEvent(CustomEvent event) throws IOException {
		fcmService.sendMessageTo(event.getFcmToken(), event.getNotificationType(), event.getMessage(),
			Long.parseLong(event.getUid()), event.getMemoryId());
	}
}
