package cmc.mellyserver.mellyapi.common.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import cmc.mellyserver.mellycore.common.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomEventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	public void publish(String fcmToken, NotificationType notificationType, String message, String uid, Long memoryId) {
		if (!fcmToken.isBlank()) {
			log.info("Publishing custom event");
			CustomEvent customEvent = new CustomEvent(this, fcmToken, notificationType, message, uid, memoryId);
			applicationEventPublisher.publishEvent(customEvent);
		}

	}
}
